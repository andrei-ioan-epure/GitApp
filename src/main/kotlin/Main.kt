import java.util.*


fun main(args: Array<String>) {

    val blob1=Blob(fileName = "file1.txt", data = "data 1")
    val blob2=Blob(fileName = "file2.txt", data = "data 2")
    val blob3=Blob(fileName = "file3.txt", data = "data 3")
    val blob4=Blob(fileName = "file4.txt", data = "data 4")

    val untrackedFiles:MutableList<Blob> = mutableListOf()
    untrackedFiles.add(blob1)
    untrackedFiles.add(blob2)
    untrackedFiles.add(blob3)
    untrackedFiles.add(blob4)


    var tree: Tree?
    var git:Git?=null

    while (true) {
        val input = readLine()?.split(" ") ?: emptyList()
        if (input.isEmpty()) {
            println()
            continue
        }
        try{
            when (input[0]) {
                "git" -> {
                    if(input.size>1)
                        when (input[1]) {

                            "init" -> {
                                println("Initialized")
                                tree=Tree("master")
                                git=Git(tree=tree, current = tree, untrackedFiles = untrackedFiles)
                            }

                            "show" -> {
                                println("\nAll branches:")
                                git?.show()
                            }

                            "status" -> {
                                git?.status()
                            }

                            "checkout" -> {

                                val branchName = input.last()
                                git?.checkout(branchName=branchName, create = input.contains("-b"))
                                println("Switched to $branchName")

                            }

                            "add" -> {
                                if(input.size>3)
                                {
                                    val files= input.subList(2,input.size).joinToString(" ")
                                    git?.add(addAll = input.contains("."),file=files)

                                }
                                else{
                                val file = input.last()
                                git?.add(addAll = input.contains("."),file=file)
                                 }
                            }

                            "commit" -> {
                                if(input.size>=4)
                                {
                                    val message= input.subList(3,input.size).joinToString(" ").replace("\"","")
                                    git?.commit(Commit(author = "me", time = Date(), message = message, tree = git.getCurrentNode()))
                                }
                            }

                            "push" -> {
                                git?.push()
                                println("\tData pushed:")
                                git?.getCurrentNode()?.showBlobs()
                                println("\n")
                            }

                            "history" -> {
                                println("\nCommit history for branch ${git?.getCurrentNode()?.getName()}()}:")

                                git?.commitHistory()?.forEachIndexed { index, commit ->
                                    println("Commit ${index + 1}:")
                                    println("\tId: ${commit.getID()}")
                                    println("\tAuthor: ${commit.author}")
                                    println("\tMessage: ${commit.message}")
                                    println("\tCommit Time: ${commit.time}")
                                }
                            }
                            "find" ->
                            {
                               val commit= git?.findCommitById(input.last())
                                if(commit!=null) {
                                    println("\nCommit for branch ${commit.tree.getName()}:")
                                    println("\tId: ${commit.getID()}")
                                    println("\tAuthor: ${commit.author}")
                                    println("\tMessage: ${commit.message}")
                                    println("\tCommit Time: ${commit.time}")
                                    println("\tData:")
                                    commit.tree.showBlobs()
                                    println("\n")
                                }
                            }

                            else -> println("${input.subList(0,2).joinToString(" ")} : invalid command")

                        }
                    else
                        println("These are common Git commands used in various situations: \ninit" +
                                "\nadd\nstatus\ncheckout\ncommit\npush\nhistory\nshow\nquit\n")            }

                "quit" -> break
                ""-> continue
                else -> println("${input[0]} : command not found")

                }
            }
            catch (e:Exception)
            {
                println(e.message)
            }

        }
}