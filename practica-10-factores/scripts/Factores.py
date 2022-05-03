class Variable:

    def __init__(self,nombre,valores_posibles):
        self.__nombre = nombre
        valores = []
        for i in valores_posibles:
          valores.append(str(i))
        self.__valores_posibles = valores
    
    def __str__(self):
        return "Nombre: " + self.__nombre + " | Valores: " + str(self.__valores_posibles)
    
    @property
    def nombre(self):
      return self.__nombre

    @property
    def valores_posibles(self):
      return self.__valores_posibles

class Factor:

    def __init__(self,alcance,valores):
        self.__alcance = alcance
        self.__valores = valores

    def __str__(self):
        #Cadena con las variables
        vars = "--------------\n"
        for i in self.__alcance:
          vars += str(i) + " \n"
        vars += "--------------\n"
        #Cadena con la tabla
        table = " "
        for j in self.__alcance:
          table += j.nombre + " "
        table += " | Prob\n"

        tab = self.creaTabla()
        x = self.transforma(tab)
        for k in range (0,len(tab)):
          table += " " + tab[k] + " | " + str(self.__valores[k]) + "\n"
        return "Factor \nVariables:\n" + vars + table
    
    #Funciones Auxiliares

    #Polinomio de direccionamiento.
    def poli_direccion(self,dicc):
      i0 = self.__alcance[0].valores_posibles.index(dicc.get(self.__alcance[0].nombre))
      indice = i0
      for i in range(1,len(self.__alcance)):
        ik = self.__alcance[i].valores_posibles.index(dicc.get(self.__alcance[i].nombre))
        tk = len(self.__alcance[i].valores_posibles)
        indice *= tk
        indice += ik

      return indice

    #Devuelve una lista con todas las posibles asignaciones de variables.
    def creaTabla(self):
      simbolos = [l.valores_posibles for l in self.__alcance]
      tabla = [str(s) for s in simbolos[0]]
      for i in range(0, len(self.__alcance)-1):
          tabla_nueva = []
          for snum in tabla:
              tabla_nueva.extend(snum + " " + str(s) for s in simbolos[i+1])
          tabla = tabla_nueva

      return tabla
    
    #Tranforma la lista con todas las posibles asignaciones de variables, en una lista
    #de listas para poder trabajar con cada asignación de variables.
    def transforma(self,tabla):
      lista = []
      for i in tabla:
        asigna = i.split()
        lista.append(asigna)
      return lista
    
    #Definición de las operaciones con factores.
    def normalize(self):
        suma = sum(self.__valores)
        for i in range (0,len(self.__valores)):
          self.__valores[i] *= 1/suma
        return self   
    
    def reduce(self,variable,valor):
      valor = str(valor)
      listvar = list(self.__alcance)
      valores_pos = []
      indice = -1
      for i in range (0,len(listvar)):
        if listvar[i].nombre == variable:
          indice = i
          listvar.remove(listvar[i])
          valores_pos = listvar[i].valores_posibles
          break

      tabla = self.transforma(self.creaTabla())
      nuevos_valores = []
      for j in range (0,len(tabla)):
        if tabla[j][indice] == valor:
          nuevos_valores.append(self.__valores[j])

      factorRes = Factor(listvar,nuevos_valores)
      return factorRes

    def multiply(self,fact):
      nuevo_alcance = list(set().union(self.__alcance,fact.__alcance))
      nuevos_valores = []
      if len(nuevo_alcance) == len(self.__alcance)+len(fact.__alcance):
        for i in self.__valores:
          for j in fact.__valores:
            nuevos_valores.append(i*j)
        factorRes = Factor(nuevo_alcance,nuevos_valores)
      else:
        #Falta implementar el caso cuando ambos factores comparten variables.
        factorRes = None
      return factorRes
    
    def marginalize(self,variable):
      listvar = list(self.__alcance)
      valores_pos = []
      for i in listvar:
        if i.nombre == variable:
          listvar.remove(i)
          valores_pos = i.valores_posibles
      lista = self.transforma(self.creaTabla())
      for i in valores_pos:
        for j in lista:
          for k in j:
            if k == i:
              j.remove(k)
      dicc = {}
      dim = 1
      for i in listvar:
        dim *= len(i.valores_posibles)
      listaVals = [0]*(dim)
      factorRes = Factor(listvar,listaVals)
      for i in range(0,len(self.__valores)):
        for j in range (0,len(listvar)):
          dicc[listvar[j].nombre] = lista[i][j]
        factorRes.__valores[factorRes.poli_direccion(dicc)] += self.__valores[i]
      return factorRes 