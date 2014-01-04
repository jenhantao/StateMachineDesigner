### Imports ###
import sys;

### Classes ###
class Node:
	children = [];
	def __init__(self,aLetter = None):
		self.letter = aLetter;
### Arguments ###
inputFileName = sys.argv[1];

inputFile = open(inputFileName);
input = inputFile.readlines();

# generate graph
words = [];
for line in input:
	words.append(line.strip());
root = Node(); # root has no letters because there are multiple starting letters

# add words to graph
for i in range(len(words)):
	currentNode = root;
	letterArray = list(words[i]);
	previousLetter = "*";
	print("current word: "+words[i]);
	for j in range(len(letterArray)):
		print("current letter: "+ str(j)+" "+letterArray[j]);
		createNew = True;
		currentLetter = letterArray[j];
		for child in currentNode.children:
			if child.letter == currentLetter and not previousLetter== currentLetter:
				currentNode = child;
				createNew = False;
				break;
		if createNew:
			newNode = Node(currentLetter);
			currentNode.children.append(newNode)
			currentNode = newNode;
			print "adding new node"
		previousLetter = currentLetter
