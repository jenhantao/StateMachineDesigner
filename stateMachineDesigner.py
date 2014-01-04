### Imports ###
import sys

### Classes ###
class Node:
	def __init__(self, aLetter = None, aLevel = 0, aParent = None, aState = 0):
		self.letter = aLetter
		self.level = aLevel
		self.children = []
		self.parent = aParent
		self.neighbors = []
		self.state = aState
	def printNode(self):
		print("level: "+str(self.level)+" letter: "+str(self.letter) + " state: " + str(self.state))
		for child in self.children:
			child.printNode()
### Arguments ###
inputFileName = sys.argv[1]

inputFile = open(inputFileName)
input = inputFile.readlines()

# generate graph
words = []
for line in input:
	words.append(line.strip())
root = Node() # root has no letters because there are multiple starting letters

# add words to graph
for i in range(len(words)):
	currentNode = root
	letterArray = list(words[i])
	previousLetter = "*"
	for j in range(len(letterArray)):
		createNew = True
		currentLetter = letterArray[j]
		for child in currentNode.children:
			if child.letter == currentLetter and child.level ==j+1:
				currentNode = child
				createNew = False
				break
		if createNew:
			newNode = Node(currentLetter,currentNode.level+1,currentNode)
			currentNode.children.append(newNode)
			currentNode = newNode
		previousLetter = currentLetter
# gather the neighbors for each node - this is used to determine the recognition sites recognized by each recombinase
# assign state numbers
# also print out the graphviz graph
allModuleNodes = [];
queue = []
queue.append(root)
count = 0;
while queue:
	currentNode = queue[0]
	queue.remove(currentNode)
	if not currentNode.letter == None:
		allModuleNodes.append(currentNode)
	currentNode.state = count;
	count = count + 1;
 	for child in currentNode.children:
 		queue.append(child)
		for neighbor in currentNode.children:
			if not child.letter == neighbor.letter:
				child.neighbors.append(neighbor)
# print out pigeon designs
for node in allModuleNodes:
	#partType partName color
	print "##### MODULE "+str(node.parent.state)+"-"+str(node.state)+" #####\n### PIGEON CODE ###"
	if node.level==1:
		print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\nC C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
	else:
		print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\nC C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
# print out what each gene/recombinase does
	print "### GENE FUNCTION ###"
	if len(node.children) > 0:
		# gene is a recombinase
		print "C"+str(node.state)+str(node.letter)+" is a recombinase recognizing the following sites:"
		print str(node.state)+str(node.letter)+"p"
		for neighbor in node.neighbors:
			print str(neighbor.state)+str(neighbor.letter)+"p"
		for child in node.children:
			print str(child.state)+str(child.letter)+"t"
	else:
		# gene is a reporter
		parent = node.parent
		word = node.letter
		while not parent == None:
			if not parent.letter == None:
				word = str(parent.letter)+word
			parent = parent.parent
		print "C"+str(node.state)+str(node.letter)+ " is a reporter for word: "+word

# print out the graphviz graph
print "##### GRAPHVIZ ###\ndigraph{"
queue = []
queue.append(root)
while queue:
	currentNode = queue[0]
	queue.remove(currentNode)
 	for child in currentNode.children:
 		queue.append(child)
		print str(currentNode.state)+ " -> "+str(child.state)+"[label=\""+str(child.letter)+"\"]"
print "}"
