import exceptions.NotFoundException

class Tree(
    private var name:String,
    private val nodes: MutableList<Tree> = mutableListOf(),
    private val blobs: MutableList<Blob> = mutableListOf()

): GitObject(name,nodes) {

    fun getName(): String = this.name

    fun getBlobs(): MutableList<Blob> = this.blobs


    fun forEachDepthFirst(node: Tree, level: Int = 0) {
        println("${" ".repeat(level)}${node.name}")
        node.nodes.forEach {
            forEachDepthFirst(it, level + 1)
        }
    }

    fun addNode(source: String, newBranchName: String) {
        val sourceNode = findNodeByName(source)
            ?: throw NotFoundException("Branch '$source' not found")

        sourceNode.nodes.add(Tree(newBranchName, mutableListOf()))

    }

    fun findNodeByName(name: String): Tree? {
        if (this.name == name) {
            return this
        }
        nodes.forEach { node ->
            val foundNode = node.findNodeByName(name)
            if (foundNode != null) {
                return foundNode
            }
        }
        return null
    }

    fun selectNode(name: String): Tree {
        return findNodeByName(name)
            ?: throw NotFoundException("Branch '$name' does not exist")

    }

    fun addBlob(blob: Blob) {
        this.blobs.add(blob)
    }

    fun showBlobs() {
        this.blobs.forEach {
            println("Id : ${it.getID()}\nFile : ${it.fileName} \nData : ${it.data}")
        }
    }

}