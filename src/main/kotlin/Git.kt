import exceptions.AlreadyExistsException
import exceptions.NotFoundException

class Git(
    private var tree:Tree,
    private var current:Tree,
    private val commits:MutableList<Commit> = mutableListOf(),
    private var untrackedFiles:MutableList<Blob> = mutableListOf(),
    private var trackedFiles:MutableList<Blob> = mutableListOf()
)
{
    init {
        this.current=this.tree
    }

    fun checkout(branchName:String,create:Boolean){

        current = if(!create) {
            tree.selectNode(name = branchName)
        }
        else {
            tree.findNodeByName(name = branchName)?.let {
                throw AlreadyExistsException("Branch '$branchName' already exists")
            }
            current.addNode(source = current.getName(), newBranchName = branchName)
            current.selectNode(name = branchName)
        }
    }
    fun status()
    {
        println("\nCurrent branch:${current.getName()}")

        if(this.untrackedFiles.isNotEmpty()) {
            println("\nUntracked files:")
            this.untrackedFiles.forEach { file ->
                println("${file.fileName}\n${file.data}")
            }
        }
        if(this.trackedFiles.isNotEmpty()) {
            println("\nTracked files:")
            this.trackedFiles.forEach { file ->
                println("${file.fileName}\n${file.data}")
            }
        }
    }
    fun add(addAll:Boolean,file:String?=null)
    {
       if(addAll)
       {
           this.trackedFiles.addAll(this.untrackedFiles)
           this.untrackedFiles = mutableListOf()
       }
        else
       {
           file?.split(" ")?.forEach {
               val blob = this.untrackedFiles.firstOrNull { unSolvedFile->unSolvedFile.fileName == it }
                   ?: throw NotFoundException("Can't find file $file")

               this.untrackedFiles.remove(blob)
               this.trackedFiles.add(blob)

           }
       }

    }
    fun getUntrackedFiles()=this.untrackedFiles
    fun setUntrackedFiles(untrackedFiles: MutableList<Blob>){this.untrackedFiles=untrackedFiles}

    fun getTrackedFiles()=this.trackedFiles

    fun getCurrentNode():Tree=this.current

    fun show(){
        tree.forEachDepthFirst(tree)
    }
    fun commit(commit: Commit) {
        commits.add(commit)
        commits.sortBy { it.time }
    }

    fun push()
    {
        this.trackedFiles.forEach{
            val existingBlob = this.current.getBlobs().find { blob -> blob.fileName == it.fileName }
                if(existingBlob==null)
            {
                this.current.addBlob(it)

            }
                else{
                    this.current.getBlobs().remove(existingBlob)
                    this.current.addBlob(it)
            }
        }
        this.trackedFiles= mutableListOf()
    }

    fun commitHistory(): List<Commit> = commits.filter { it.tree==current }

    fun findCommitById(id: String): Commit {
        return commits.firstOrNull { it.getID() == id }
            ?: throw NotFoundException("Commit with id $id not found")
    }

}