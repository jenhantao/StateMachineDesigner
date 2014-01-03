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
while len(words) > 0:
	word = words[0];
	currentNode = root;

	for letter in list(word):
		needNew = True;
		if len(currentNode.children) > 0:
			count = 0;
			for child in currentNode.children:
				count = count + 1;
				if child.letter == letter:
					currentNode = child;
					needNew = False;
				if count == len(currentNode.children):
					needNew = True		
				print("count: "+str(count) + " length: " + str(len(currentNode.children))+ " letter: "+str(currentNode.letter))
		else:
			needNew = True;
		print needNew
		if needNew:
			newNode = Node(letter);
			currentNode.children.append(newNode);
			currentNode = newNode;
	if len(words) > 0:
		del words[0]
