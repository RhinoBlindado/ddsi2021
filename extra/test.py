


# LIBRARIES
#   - Get parameters from command line.
import sys

# CLASSES
class FuncDep:
    """
    AAA
    """
    def __init__(self, left, right):
        """
        Initializer 
        """
        self.left = left
        self.right = right

    def setLeft(self, newLeft):
        """
        docstring
        """
        self.left = newLeft
    
    def setRight(self, newRight):
        """
        docstring
        """
        self.right = newRight

    def getLeft(self):
        """
        bruh
        """
        return self.left

    def getRight(self):
        """
        """
        return self.right


    def checkIndependence(self, attr):
        """
        
        """
        isIn = False

        for i in self.left:
            if i == attr:
                isIn = True

        for i in self.right:
            if i == attr:
                isIn = True
        
        return isIn

    def checkIsOnLeft(self, attr):
        """
        """
        isIn = False

        for i in self.left:
            if i == attr:
                isIn = True

        return isIn

    def checkIsOnRight(self, attr):
        """
        """
        isIn = False

        for i in self.right:
            if i == attr:
                isIn = True

        return isIn
        
    def checkIsIn(self, attr):
        isIn = False

        for i in self.right:
            if i == attr:
                isIn = True

        for i in self.left:
            if i == attr:
                isIn = True

        return isIn        

    def display(self):
        """
        docstring
        """
        print(self.left+"->"+self.right, end='')

# FUNCTIONS
def candidateKeyCalc():
    """
    bruh
    """
    sys.argv.pop(0)

    reading = True
    R = []

    while(reading):

        reader = sys.argv.pop(0)

        if(reader == '*'):
            reading = False
        else:
            R.append(reader)

    F = []
    reading = True
    while(reading):

        if(sys.argv[0] == '*' or sys.argv[1] == '*'):
            reading = False
        else:
            left = sys.argv.pop(0)
            right = sys.argv.pop(0)
            F.append( FuncDep(left, right) )

    print("\tR = { ",end='')

    for i in R:
        print(i+" ", end='')
    
    print("}")

    print("\tF = { ", end='')
    for i in F:
        i.display()
        print(" ", end='')

    print("}")

    # Check for Independent Attributes
    attrIndependent = []
    for i in R:
        isInside = False
        for j in F:
            if(j.checkIndependence(i)):
                isInside = True
        if(not isInside):
            attrIndependent.append(i)
            R.remove(i)

    print("\tAtributos Independientes: ", end='')
    if(len(attrIndependent)):
        for i in attrIndependent:
            print(i, end='')
        print("")
    else:
        print("No hay.")
    
    # Check for Equivalent Attributes
    attrEquivalent = []

    # Check for Determinant not Dependent / Determinants and Dependents / Depentends not Determinant

    attrDeterminant = []
    attrDependent = []
    attrDeterDepen = []

    for i in R:

        countLeft = 0
        countRight = 0

        for j in F:
            #print("CHECK: "+i+" "+str(j.checkIsIn(i)))
            if(j.checkIsIn(i)):
                if(j.checkIsOnLeft(i)):
                    countLeft += 1

                if(j.checkIsOnRight(i)):
                    countRight += 1

        if(countLeft and not countRight):
            attrDeterminant.append(i)
        elif(countRight and not countLeft):
            attrDependent.append(i)
        else:
            attrDeterDepen.append(i)

    
    print("\tAtributos Determinantes no Determinados: ", end='')
    if(len(attrDeterminant)):
        for i in attrDeterminant:
            print(i, end=' ')
        print("")

    else:
        print("No hay.")

    print("\tAtributos Determinantes y Determinados:  ", end='')
    if(len(attrDeterDepen)):
        for i in attrDeterDepen:
            print(i, end=' ')
        print("")

    else:
        print("No hay.")


    print("\tAtributos Determinados no Determinantes: ", end='')
    if(len(attrDependent)):
        for i in attrDependent:
            print(i, end=' ')
        print("")

    else:
        print("No hay.")


    print("-- ALGORITMO DE CÁLCULO DE CLAVES CANDIDATAS --")
    print("\t1. Eliminación de Atributos Independientes")

    print("\t2. Eliminación de Atributos Equivalentes")

    print("\t3. Procesamiento de Atributos Determinantes no Determinados")

    if(len(attrDeterminant)):

        if(len(attrDeterminant) > 1):
            pass
        

        print("\t Kp = {", end='')

        for i in attrDeterminant:
            

    print("\t4. Procesamiento de Atributos Determinados y Determinantes")

    print("\t5. Incorporación de Atributos Independientes")

    print("\t6. Incorporación de Atributos Equivalentes")


#endFunction

#   Main Function
def main():
    """
    docstring
    """
    print("--- SOLVER ---")
#    print ('Number of arguments:', len(sys.argv), 'arguments.')
    print ('Argument List:', str(sys.argv))

    if (sys.argv.pop(0) == '0'):
        candidateKeyCalc()



#   ???
if __name__ == "__main__":
    sys.argv.pop(0)
    main()
    
