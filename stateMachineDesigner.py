### Imports ###
import sys

### Classes ###
class Node:
	def __init__(self,aLetter = None,aLevel=-1):
		self.letter = aLetter
		self.level = aLevel
		self.children = []
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
	#print("current word: "+words[i])
	for j in range(len(letterArray)):
		#print("current letter: "+ str(j)+" "+letterArray[j])
		createNew = True
		currentLetter = letterArray[j]
		for child in currentNode.children:
			if child.letter == currentLetter and child.level ==j:
				currentNode = child
				createNew = False
				break
		if createNew:
			newNode = Node(currentLetter,currentNode.level+1)
			currentNode.children.append(newNode)
			currentNode = newNode
			#print "******** adding new node ********"
		previousLetter = currentLetter
def printGraph(root):
	queue = []
	queue.append(root)
	while queue:
		currentNode = queue[0]
		queue.remove(currentNode)
		print(str(currentNode.level)+" "+str(currentNode.letter))
		for child in currentNode.children:
			queue.append(child)
printGraph(root)

