# ------ Auxiliares -----

import copy
def clona(obj):
    """ Atajo para crear copias de objetos recursivamente. """
    return copy.deepcopy(obj)

def _lista_a_dict(l):
    """ Pone a todos los elementos de la lista en un diccionario cuya
    llave es el nombre del objeto.
    Sólo sirve para listas de objetos con atributo 'nombre'.
    """
    d = {}
    for o in l:
        d[o.nombre] = o
    return d

# ------ Dominio -----

class Dominio:
    """ Clase para definir el dominio, o espacio de estados en el cual se plantearán problemas de planeación. """
    def __init__(self, nombre, tipos, predicados, acciones):
        """
        Inicializa un dominio
        :param nombre:
        :param tipos:
        :param predicados:
        :param acciones:
        """
        self.nombre = nombre
        self.tipos = tipos
        self.predicados = predicados
        self.acciones = acciones

        self._predicados = _lista_a_dict(predicados)

    def __str__(self):
        dic = {'name':          self.nombre,
               'types':         "\n        ".join(self.tipos),
               'predicates':    "\n        ".join(str(p) for p in self.predicados),
               'actions':       "\n    ".join(str(a) for a in self.acciones)
               }
        return """
(define (domain {name})
    (:requirements :strips :typing)
    (:types
        {types}
    )
    (:predicates
        {predicates})
    )
    {actions}
)
        """.format(**dic)

    def declaración(self, nombre):
        """ 
        Devuelve la declaración del predicado con el nombre indicado.
        """
        return self._predicados[nombre]


class Objeto:
    """ Valor concreto para variables en el dominio. """
    def __init__(self, nombre, tipo):
        """
        Crea un objeto existente en el dominio para este problema.
        :param nombre: Símbolo del objeto
        :param tipo: tipo del objeto
        """
        self.nombre = nombre
        self.tipo = tipo

    def __str__(self):
        return "{} - {}".format(self.nombre, self.tipo)
    
    def __eq__(self,other):
        return self.nombre == other.nombre and self.tipo == other.tipo


class Variable:
    """ Variable tipada. """
    def __init__(self, nombre, tipo, valor=None):
        """
        :param nombre: símbolo nombre de esta variable.  Los nombres de variables inician con ?
        :param tipo: tipo de la variable, debe estar registrado en la descripción del dominio
        :param valor: objeto vinculado a esta variable, si es None la variable está libre
        """
        self.nombre = nombre
        self.tipo = tipo
        self._valor = valor

    @property
    def valor(self):
        return self._valor

    @valor.setter
    def valor(self, valor_nuevo):
        """
        Permite asignar None o un valor de tipo de esta variable.
        """
        if valor_nuevo and valor_nuevo.tipo != self.tipo:
            raise Exception(f"{valor_nuevo} no es de tipo {self.tipo}")
        self._valor = valor_nuevo

    @valor.deleter
    def valor(self):
        self._valor = None

    def __str__(self):
        if self.valor:
            return self.valor.nombre
        return "{} - {}".format(self.nombre, self.tipo)
    
    def __eq__(self,other):
        return self.nombre == other.nombre and self.tipo == other.tipo and self._valor == other._valor
        
        
class Formula:
    """
    Predicado o fórmula generada.
    """
    pass


class Predicado(Formula):
    """ Representa un hecho. """
    def __init__(self, declaracion, variables):
        """
        Predicados para representar hechos.
        :param predicado: declaración con los tipos de las variables.
        :param variables: lista de instancias de variables en la acción donde se usa este predicado.
        :param negativo: indica un predicado del tipo "no P", utilizable para especificar efectos o metas.
        """
        self.declaracion = declaracion
        self.variables = variables

    def __str__(self):
        pred = "({0} {1})".format(self.declaracion.nombre, " ".join(v.valor.nombre if v.valor else v.nombre for v in self.variables))
        return pred

    def __eq__(self,other):
        return self.declaracion == other.declaracion and self.variables == other.variables 

class DeclaracionDePredicado:
    """ Representa un hecho. """
    def __init__(self, nombre, variables):
        """
        Declaración de predicados para representar hechos.
        :param nombre:
        :param variables: lista de variables tipadas
        """
        self.nombre = nombre
        self.variables = variables

    def __str__(self):
        pred = "({0} {1})".format(self.nombre, " ".join(str(v) for v in self.variables))
        return pred

    def _verifica_tipos(self, variables):
        """
        Las variables en la lista deben tener los mismos tipos, en el mismo orden, que este predicado.
        """
        for dec, var in zip(self.variables, variables):
            if (dec.tipo != var.tipo):
                raise Exception(f"Los tipos de las variables {dec} y {var} no coinciden")

    def __call__(self, *args):
        """
        Crea un predicado con las variables o valores indicados y verifica que sean del tipo
        correspondiente a esta declaración.

        Cuando se usa dentro de una acción las variables deben ser las mismas instancias para todos
        los predicados dentro de la misma acción.
        """
        variables = []
        for var, arg in zip(self.variables, args):
            if isinstance(arg, Objeto):
                # print("instancia ", arg)
                temp_v = clona(var)
                temp_v.valor = arg
                variables.append(temp_v)
            elif isinstance(arg, Variable):
                # print("variable ", arg)
                variables.append(arg)
            else:
                print("Ni lo uno ni lo otro ", arg, " tipo ", type(arg))

        return Predicado(self, variables)


class No(Formula):
    """
    Negación de un predicado.
    """
    def __init__(self, predicado):
        super().__init__()
        self.predicado = predicado

    def __str__(self):
        return "(not {0})".format(str(self.predicado))



class Acción:
    """ Función de transición con su acción correspondiente. """
    def __init__(self, nombre, parámetros, variables, precondiciones, efectos):
        """
        Inicializa definición de la función de transición para esta acción.
        :param nombre: nombre de la acción
        :param parámetros: lista de variables tipadas
        :param variables: lista de variables libres que pueden tomar su valor de cualquier objeto del domino siempre que
               sus valores satisfagan las restriciones de las precondiciones.
        :param precondiciones: lista de predicados con variables libres
        :param efectos: lista de predicados con variables libres
        """
        self.nombre = nombre
        self.parámetros = parámetros
        self.vars = variables
        self.precondiciones = precondiciones
        self.efectos = efectos

    def __str__(self):
        dic = {'name':      self.nombre,
               'params':    " ".join(str(p) for p in self.parámetros),   # Podrían reunirse 1o los de tipos iguales
               'prec':      " ".join(str(p) for p in self.precondiciones),
               'efec':      " ".join(str(p) for p in self.efectos)
               }

        if self.vars:
            dic['vars'] = "\n        :vars         ({})".format(" ".join(str(v) for v in self.vars))
        else:
            dic['vars'] = ""

        if len(self.precondiciones) >= 2:
            dic['prec'] = "(and " + dic['prec'] + ")"

        if len(self.efectos) >= 2:
            dic['efec'] = "(and " + dic['efec'] + ")"

        return """(:action {name}
        :parameters   ({params}) {vars}
        :precondition {prec}
        :effect       {efec}
    )""".format(**dic)


# ------ Problema -----

class Problema:
    """ Definicion de un problema en un dominio particular. """
    def __init__(self, nombre, dominio, objetos, predicados, predicados_meta):
        """
        Problema de planeación en una instancia del dominio.
        :param nombre: nombre del problema
        :param dominio: referencia al objeto con la descripción genérica del dominio
        :param objetos: lista de objetos existentes en el dominio, con sus tipos
        :param predicados: lista de predicados con sus variables aterrizadas, indicando qué cosas son verdaderas en el
               estado inicial.  Todo aquello que no esté listado es falso.
        :param predicados_meta: lista de predicados con sus variables aterrizadas, indicando aquellas cosas que deben
               ser verdaderas al final.  Para indicar que algo debe ser falso, el predicado debe ser negativo.
        """
        self.nombre = nombre
        self.dominio = dominio # ref a objeto Dominio
        d_objetos = {}
        for objeto in objetos:
            if objeto.tipo not in d_objetos:
                d_objetos[objeto.tipo] = [objeto]
            else:
                d_objetos[objeto.tipo].append(objeto)
        self.d_objetos = d_objetos
        self.estado = predicados
        self.meta = predicados_meta

    def __str__(self):
        dic = {'name':          self.nombre,
               'domain_name':   self.dominio.nombre,
               'objects':       "\n      ".join(" ".join(o.nombre for o in self.d_objetos[tipo]) + " - " + tipo for tipo in self.d_objetos),
               'init':          "\n      ".join(str(p) for p in self.estado),
               'goal':          "\n      ".join(str(p) for p in self.meta)}
        if len(self.meta) >= 2:
            dic['goal'] = "(and " + dic['goal'] + ")"
        return """(define (problem {name}
    (:domain {domain_name})
    (:objects
      {objects})
    (:init
      {init})
    (:goal
      {goal})
)
        """.format(**dic)

def aplicable(problema,accion,params):
    # Accion(nombre, params(lista de vars), variables(lista de vars), precondiciones(lista de predi), efectos(lista de predi))
    #params es una lista de objetos
    precondiciones = accion.precondiciones
    
    return 0

# PUNTO 4
# Metodo que determina si un estado satisface las condiciones indicadas en el campo meta.
def satisfaceMeta(estado, meta):
    for i in meta:
        if i in estado:
            continue
        else:
            return False
    return True


if __name__ == '__main__':

    # PUNTO 2: CREAR LOS OBJETOS CORRESPONDIENTES AL DOMINIO Y PROBLEMA DADOS E IMPRIMIRLOS.
 
    # Predicados:
    p_sostiene = DeclaracionDePredicado('sostiene',[Variable('?k','grúa'),Variable('?c','contenedor')])
    p_libre = DeclaracionDePredicado('libre',[Variable('?k','grúa')])
    p_en = DeclaracionDePredicado('en',[Variable('?c','contenedor'),Variable('?p','pila')])
    p_hasta_arriba = DeclaracionDePredicado('hasta_arriba',[Variable('?c','contenedor'),Variable('?p','pila')])
    p_sobre = DeclaracionDePredicado('sobre',[Variable('?k1','contenedor'),Variable('?k2','contenedor')])
    # --- toma ----
    # Parametros
    av_g = Variable('?k','grúa')
    av_c = Variable('?c','contenedor')
    av_p = Variable('?p','pila')
    # Variables
    av_oc  =  Variable('?otro','contenedor')
    # Accion
    a_toma = Acción('toma',[av_g,av_c,av_p],[av_oc],
                            [p_libre(av_g),p_en(av_c,av_p),p_hasta_arriba(av_c,av_p),p_sobre(av_c,av_oc)],
                            [p_sostiene(av_g,av_c),p_hasta_arriba(av_oc,av_p),No(p_en(av_c,av_p)),
                            No(p_hasta_arriba(av_c,av_p)),No(p_sobre(av_c,av_oc)),No(p_libre(av_g))])

    # --- pon ---
    # Parametros
    # Variables
    # Accion
    a_pon = Acción('pon',[av_g,av_c,av_p],[av_oc],
                        [p_sostiene(av_g,av_c),p_hasta_arriba(av_oc,av_p)],
                        [p_en(av_c,av_p),p_hasta_arriba(av_c,av_p),p_sobre(av_c,av_oc),
                        No(p_hasta_arriba(av_oc,av_p)),No(p_sostiene(av_g,av_c)),p_libre(av_g)])                    

    # Dominio
    dominio1 = Dominio('platform-worker-robot',
                        ['contenedor','pila','grúa'],
                        [p_sostiene,p_libre,p_en,p_hasta_arriba,p_sobre],
                        [a_toma,a_pon])
    print(dominio1)
    # Objetos
    k1 = Objeto('k1','grúa')
    k2 = Objeto('k2','grúa')
    p1 = Objeto('p1','pila')
    q1 = Objeto('q1','pila')
    p2 = Objeto('p2','pila')
    q2 = Objeto('q2','pila')
    ca = Objeto('ca','contenedor')
    cb = Objeto('cb','contenedor')
    cc = Objeto('cc','contenedor')
    cd = Objeto('cd','contenedor')
    ce = Objeto('ce','contenedor')
    cf = Objeto('cf','contenedor')
    pallet = Objeto('pallet','contenedor')
    # Predicados aterrizados
    en1 = p_en(ca,p1)
    en2 = p_en(cb,p1)
    en3 = p_en(cc,p1)
    en4 = p_en(cd,q1)
    en5 = p_en(ce,q1)
    en6 = p_en(cf,q1)
    sobre1 = p_sobre(ca,pallet)
    sobre2 = p_sobre(cb,ca)
    sobre3 = p_sobre(cc,cb)
    sobre4 = p_sobre(cd,pallet)
    sobre5 = p_sobre(ce,cd)
    sobre6 = p_sobre(cf,ce)
    ha1 = p_hasta_arriba(cc,p1)
    ha2 = p_hasta_arriba(cf,q1)
    ha3 = p_hasta_arriba(pallet,p2)
    ha4 = p_hasta_arriba(pallet,q2)
    li1 = p_libre(k1)
    li2 = p_libre(k2)
    g_en1 = p_en(ca,p2)
    g_en2 = p_en(cb,q2)
    g_en3 = p_en(cc,p2)
    g_en4 = p_en(cd,q2)
    g_en5 = p_en(ce,q2)
    g_en6 = p_en(cf,q2)
    #Problema
    problema1 = Problema('dwrpb1',dominio1,[k1,k2,p1,q1,p2,q2,ca,cb,cc,cd,ce,cf,pallet],
                            [en1,en2,en3,en4,en5,en6,sobre1,sobre2,sobre3,sobre4,sobre5,sobre6,ha1,ha2,ha3,ha4,
                            li1,li2],
                            [g_en1,g_en2,g_en3,g_en4,g_en5,g_en6])
    print(problema1)

    # PUNTO 5: PRUEBAS
    #Prueba de un estado que satisface las condiciones del campo meta.
    print("¿El estado satisface la meta?:",satisfaceMeta([g_en1,g_en2,g_en3,g_en4,g_en5,g_en6],[g_en1,g_en2,g_en3,g_en4,g_en5,g_en6]))
    #Prueba de un estado que satisface las condiciones del campo meta.
    print("¿El estado satisface la meta?:",satisfaceMeta([g_en1,g_en2,g_en3,g_en5,g_en6],[g_en1,g_en2,g_en3,g_en4,g_en5,g_en6]))
