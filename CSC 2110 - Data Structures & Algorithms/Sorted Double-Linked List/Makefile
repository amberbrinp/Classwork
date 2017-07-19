AutomatedMakefile = am
CC = g++

FILES = HybridDriver.o
EXECUTABLE = hybrid.exe

PROJECT_PATH = $(PROJECT_DIR)

INC_DIRS = -I$(PROJECT_PATH)/CSC2110/ 
LIB_DIRS = -L$(PROJECT_PATH)/CSC2110/ 
LIBS = -lCSC2110 

COMPILE = $(CC) $(INC_DIRS) -c
LINK = $(CC) $(LIB_DIRS) -o

all: Project

Project: 		$(FILES)
			$(LINK) $(EXECUTABLE) $(FILES) $(LIBS)

HybridDriver.o:		Hybrid.h HybridDriver.cpp SortedListDoublyLinked.h ListDoublyLinkedIterator.h DoubleNode.h
			$(COMPILE) HybridDriver.cpp



