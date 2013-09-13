
JC = javac
JI = java
JCCC = javacc
OPTS = -target 1.6 -source 1.6 -classpath ../trollAim -d ../trollAim 

ALLCLASS = AimConstants.class Model.class  Node.class Equation.class \
	PowerNode.class \
	Aim.class CoefficientNode.class ConstantNode.class \
	ProductNode.class SumNode.class Variable.class \
	VariableNode.class 



all: 
	mkdir -p ../trollAim
	$(JC) $(OPTS) *.java
# $@ = The file name of the target of the rule.
# $< = The name of the first prerequisite.

.SUFFIXES:  .java .class .jj

.java.class:
	mkdir -p ../trollAim
	$(JC) $(OPTS) $<

.jj.java:
	$(JCCC) $<

Aim.java: Aim.jj
	$(JCCC) Aim.jj

Aim.class: Aim.java 
	mkdir -p ../trollAim
	$(JC) $(OPTS) Aim.java


clean:
	rm *.class Aim.java 
	rm -rf ../lib 
	rm -rf ../trollAim
cleanObsolete:
	rm TokenMgrError.java Token.java ParseException.java SimpleCharStream.java
trollAim.jar:	Aim.class
	(cd ../trollAim; jar cvf /tmp/trollAim.jar trollAim)
	mkdir -p ../lib
	mv /tmp/trollAim.jar ../lib


