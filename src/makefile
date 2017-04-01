JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	MinHeapNode.java \
	BitWriter.java \
	BinaryHeap.java \
	FourWayHeap.java \
	PairingHeap.java \
	decoder.java \
	encoder.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class