### Imports ###
import sys

### Classes ###
class Node:
	def __init__(self, aLetter = None, aLevel = 0, aParent = None, aState = 0):
		self.letter = aLetter
		self.level = aLevel
		self.children = []
		self.parent = aParent
		self.neighbors = [] # nodes with the same level as this ndoe
		self.state = aState
	def printNode(self):
		print("level: "+str(self.level)+" letter: "+str(self.letter) + " state: " + str(self.state))
		for child in self.children:
			child.printNode()
### Arguments ###
inputFileName = sys.argv[1]
fileNameRoot = inputFileName.split(".")[0]
if len(sys.argv) > 2:
	if sys.argv[2] == "n":
		nested = True # recombinase sites are nested
	elif sys.argv[2] == "m":
		multi = True # recombinase can recognize multiple sites

inputFile = open(inputFileName)
input = inputFile.readlines()
inputFile.close()
# determine what type of input is being read
# if there are spaces in the first line, then the input is a series of edges
if " " in input[0]:
	wordList = False
else:
	wordList = True
# if the input has no spaces in the first line, then the input is a series of words

# generate graph
root = Node() # root has no letters because there are multiple starting letters
if wordList:
	# use the word list
	words = []
	for line in input:
		words.append(line.strip())
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
else:
	# use the edgeList
	nodeHash = {}
	nodes = set()
	edges = []
	for line in input:
		tokens = line.strip().split()
		edges.append((tokens[0], tokens[1], tokens[2]))
		nodes.add(tokens[0])
		nodes.add(tokens[1])
	count = 0
	for nodeState in nodes:
		newNode = Node()
		newNode.state = nodeState
		nodeHash[nodeState] = newNode
	for edge in edges:
		sourceNode = nodeHash[edge[0]]
		targetNode = nodeHash[edge[1]]
		sourceNode.children.append(targetNode)
		targetNode.parent = sourceNode
		targetNode.letter = edge[2]
	for node in nodeHash.values():
		if node.parent == None:
			# first node with no parent is the root 
			root = node
			break

# for the naive file
def makeNaiveFile(root):
# gather the neighbors for each node - this is used to determine the recognition sites recognized by each recombinase
# assign state numbers
	pigeonFile = open(fileNameRoot+"_pigeon.txt",'w')
	geneFunctionFile = open(fileNameRoot+"_geneFunction.txt",'w')
	#print "### GENE FUNCTION ###"
	geneFunctionFile.write("### GENE FUNCTION ###\n")
	allModuleNodes = [];
	queue = []
	queue.append(root)
	count = 0;
	while queue:
		currentNode = queue[0]
		queue.remove(currentNode)
		if not currentNode.letter == None:
			allModuleNodes.append(currentNode)
		if wordList:
			currentNode.state = count;
			count = count + 1;
		for child in currentNode.children:
			queue.append(child)
			for neighbor in currentNode.children:
				if not child.letter == neighbor.letter:
					child.neighbors.append(neighbor)
	for node in allModuleNodes:
		#partType partName color
		#print "##### MODULE "+str(node.parent.state)+"-"+str(node.state)+" #####\n### PIGEON CODE ###"
		pigeonFile.write("##### MODULE "+str(node.parent.state)+"-"+str(node.state)+" #####\n### PIGEON CODE ###\n")
		if node.level==1:
		# starting nodes
			recombString = "" # gives pigeon code for the recombinase associated with this
			recombString += "c C" + str(node.state) + str(node.letter) + " 6\n" # for removing promoter
			# for removing terminators in child nodes
			counter = 0
			for child in node.children:
				recombString += "c C" + str(node.state) + str(node.letter) + "_c" + str(counter) + " 6\n" # for removing promoter
				counter += 1
			# for removing promoters of neighbors' children
			counter = 0
			for neighbor in node.neighbors:
				recombString += "c C" + str(node.state) + str(node.letter) + "_n" + str(counter) + " 6\n" # for removing promoter
				counter += 1
			#print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
			pigeonFile.write("t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n"+recombString+ "t t\n# Arcs\n")
		elif len(node.children) == 0:
		# leaf nodes
			#print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
			pigeonFile.write("t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs\n")
		else:
		# intermediate nodes
			recombString = "" # gives pigeon code for the recombinase associated with this
			recombString += "c C" + str(node.state) + str(node.letter) + " 6\n" # for removing promoter
			# for removing terminators in child nodes
			counter = 0
			for child in node.children:
				recombString += "c C" + str(node.state) + str(node.letter) + "_c" + str(counter) + " 6\n" # for removing promoter
				counter += 1
			# for removing promoters of neighbors' children
			counter = 0
			for neighbor in node.neighbors:
				recombString += "c C" + str(node.state) + str(node.letter) + "_n" + str(counter) + " 6\n" # for removing promoter
				counter += 1
			#print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\n"+recombString+"t t\n# Arcs"
			pigeonFile.write("t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\n"+recombString+"t t\n# Arcs\n")
	# #print out what each gene/recombinase does
		if len(node.children) > 0:
			# gene is a recombinase
			#print "C"+str(node.state)+str(node.letter)+" is a recombinase recognizing the following sites:"
			geneFunctionFile.write("C"+str(node.state)+str(node.letter)+" is a recombinase recognizing the following sites:\n")
			#print str(node.state)+str(node.letter)+"p"
			geneFunctionFile.write(str(node.state)+str(node.letter)+"p\n")
			counter = 0
			for neighbor in node.neighbors:
				#print "C"+str(node.state)+str(node.letter)+"_n"+str(counter)+" is a recombinase recognizing the following sites:"
				geneFunctionFile.write("C"+str(node.state)+str(node.letter)+"_n"+str(counter)+" is a recombinase recognizing the following sites:\n")
				#print str(neighbor.state)+str(neighbor.letter)+"p"
				geneFunctionFile.write(str(neighbor.state)+str(neighbor.letter)+"p\n")
				counter += 1
			counter = 0
			for child in node.children:
				#print "C"+str(node.state)+str(node.letter)+"_c"+str(counter)+" is a recombinase recognizing the following sites:"
				geneFunctionFile.write("C"+str(node.state)+str(node.letter)+"_c"+str(counter)+" is a recombinase recognizing the following sites:\n")
				#print str(child.state)+str(child.letter)+"t"
				geneFunctionFile.write(str(child.state)+str(child.letter)+"t\n")
				counter += 1
		else:
			# gene is a reporter
			parent = node.parent
			word = node.letter
			while not parent == None:
				if not parent.letter == None:
					word = str(parent.letter)+word
				parent = parent.parent
			#print "C"+str(node.state)+str(node.letter)+ " is a reporter for word: "+word
			geneFunctionFile.write("C"+str(node.state)+str(node.letter)+ " is a reporter for word\n: "+word+"\n")
	pigeonFile.close()
	geneFunctionFile.close()

# for the multi recognition site optimization
def makeMultiFile(root):
# gather the neighbors for each node - this is used to determine the recognition sites recognized by each recombinase
# assign state numbers
	pigeonFile = open(fileNameRoot+"_pigeon.txt",'w')
	geneFunctionFile = open(fileNameRoot+"_geneFunction.txt",'w')
	allModuleNodes = [];
	queue = []
	queue.append(root)
	count = 0;
	while queue:
		currentNode = queue[0]
		queue.remove(currentNode)
		if not currentNode.letter == None:
			allModuleNodes.append(currentNode)
		if wordList:
			currentNode.state = count;
			count = count + 1;
		for child in currentNode.children:
			queue.append(child)
			for neighbor in currentNode.children:
				if not child.letter == neighbor.letter:
					child.neighbors.append(neighbor)
	for node in allModuleNodes:
		#partType partName color
		#print "##### MODULE "+str(node.parent.state)+"-"+str(node.state)+" #####\n### PIGEON CODE ###"
		pigeonFile.write("##### MODULE "+str(node.parent.state)+"-"+str(node.state)+" #####\n### PIGEON CODE ###\n")
		if node.level==1:
			#print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
			pigeonFile.write("t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs\n")
		else:
			#print "t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs"
			pigeonFile.write("t t\n> "+str(node.state)+str(node.letter)+"p 2\np p_"+str(node.letter)+" 2\n> "+str(node.state)+str(node.letter)+"p 2\n> "+str(node.state)+str(node.letter)+"t 4\nt t 4\n> "+str(node.state)+str(node.letter)+"t 4\nc C"+str(node.state)+str(node.letter) +" 6\nt t\n# Arcs\n")
	# #print out what each gene/recombinase does
		#print "### GENE FUNCTION ###"
		geneFunctionFile.write("### GENE FUNCTION ###\n")
		if len(node.children) > 0:
			# gene is a recombinase
			#print "C"+str(node.state)+str(node.letter)+" is a recombinase recognizing the following sites:"
			geneFunctionFile.write("C"+str(node.state)+str(node.letter)+" is a recombinase recognizing the following sites:\n")
			#print str(node.state)+str(node.letter)+"p"
			geneFunctionFile.write(str(node.state)+str(node.letter)+"p\n")
			for neighbor in node.neighbors:
				#print str(neighbor.state)+str(neighbor.letter)+"p"
				geneFunctionFile.write(str(neighbor.state)+str(neighbor.letter)+"p\n")
			for child in node.children:
				#print str(child.state)+str(child.letter)+"t"
				geneFunctionFile.write(str(child.state)+str(child.letter)+"t\n")
		else:
			# gene is a reporter
			parent = node.parent
			word = node.letter
			while not parent == None:
				if not parent.letter == None:
					word = str(parent.letter)+word
				parent = parent.parent
			#print "C"+str(node.state)+str(node.letter)+ " is a reporter for word: "+word
			geneFunctionFile.write("C"+str(node.state)+str(node.letter)+ " is a reporter for word\n: "+word+"\n")
	pigeonFile.close()
	geneFunctionFile.close()


def makeNestedFile(root):
# gather the neighbors for each node - this is used to determine the recognition sites recognized by each recombinase
# assign state numbers
	pigeonFile = open(fileNameRoot+"_pigeon.txt",'w')
	geneFunctionFile = open(fileNameRoot+"_geneFunction.txt",'w')

	pigeonFile.close()
	geneFunctionFile.close()

# make the graphviz graph
def makeGraphVizFile(root):
	graphVizFile = open (fileNameRoot+"_graphviz.txt","w")
	#print "##### GRAPHVIZ ###\ndigraph{"
	graphVizFile.write("##### GRAPHVIZ ###\ndigraph{\n")
	queue = []
	queue.append(root)
	while queue:
		currentNode = queue[0]
		queue.remove(currentNode)
		for child in currentNode.children:
			queue.append(child)
			#print str(currentNode.state)+ " -> "+str(child.state)+"[label=\""+str(child.letter)+"\"]"
			graphVizFile.write(str(currentNode.state)+ " -> "+str(child.state)+"[label=\""+str(child.letter)+"\"]\n")
	#print "}"

	graphVizFile.write("}\n")
	graphVizFile.close()
#makeMultiFile(root)
makeNaiveFile(root)
# graphviz file is the same for all files
makeGraphVizFile(root)
