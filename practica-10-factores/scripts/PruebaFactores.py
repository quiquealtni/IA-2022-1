from Factores import *

#Prueba de la operacion marginalización.
Est = Variable("Estacion",['Primavera','Verano','Otoño','Invierno'])
Ll = Variable("Lluvia",[0,1])
f = Factor((Est,Ll),[0.1875,0.0625,0.075,0.175,0.175,0.075,0.2,0.05])
print("Factor 1\n",f)
f1 = f.marginalize("Estacion")
print("Factor resultante de marginalizar 'Estacion' del Factor 1\n",f1)

#Prueba de la operación normalización.
A = Variable("A",[0])
B = Variable("B",[0,1])
f3 = Factor((A,B),[0.18,0.12])
print("Factor 2\n",f3)
f3.normalize()
print("Factor resultante de normalizar el Factor 2\n",f3.normalize())

#Prueba de la operación reducción.
a = Variable("A",[0,1])
b = Variable("B",[0,1])
fa = Factor((a,b),[0.18,0.12,0.42,0.28])
print("Factor 3\n",fa)
print("Factor resultante de reducir A=0 del Factor 3\n",fa.reduce("A",0))

#Prueba para la multiplicación cuando ambos factores no comparten variables.
va = Variable("A",[0,1])
vb = Variable("B",[0,1])
vc = Variable("C",[0,1])
vd = Variable("D",[0,1])
fA = Factor((va,vb),[0.2,0.7,0.05,0.05])
fB = Factor((vc,vd),[0.6,0.2,0.1,0.1])
print("Factor 4\n",fA)
print("Factor 5\n",fB)
print("Factor resultante de la multiplicacion del Factor 4 por el Factor 5\n",fA.multiply(fB))
