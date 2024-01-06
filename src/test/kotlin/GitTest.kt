import exceptions.AlreadyExistsException
import exceptions.NotFoundException
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class GitTest {

    @Test
    fun `Checkout branch when branch does not exist`() {
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())

        val exception = assertFailsWith<NotFoundException> {
            git.checkout(branchName = "notFound", create = false)
        }
        val expectedOutput="Branch 'notFound' does not exist"
        assertEquals(expectedOutput, exception.message)

    }

    @Test
    fun `Checkout branch when branch exists`() {
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        git.checkout(branchName = "master", create = false)
        val expectedOutput="master"
        assertEquals(expectedOutput, git.getCurrentNode().getName())
    }
    @Test
    fun `Checkout create branch when branch does not exist`() {
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        git.checkout(branchName = "main", create = true)
        assertEquals("main", git.getCurrentNode().getName())
    }

    @Test
    fun `Checkout create branch when branch does exist`() {
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        git.checkout(branchName = "main", create = true)
        val exception = assertFailsWith<AlreadyExistsException> {
            git.checkout(branchName = "main", create = true)
        }
        val expectedOutput="Branch 'main' already exists"
        assertEquals(expectedOutput, exception.message)
    }

    @Test
    fun `Status after adding a blob`()
    {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val blob1=Blob(fileName = "file1.txt", data = "data 1")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.status()

        val expectedOutput = "\nCurrent branch:master\n\nUntracked files:\nfile1.txt\ndata 1\n"
        val actual = outputStream.toString().replace("\r\n", "\n")

        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `Status after adding multiple blobs`() {

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))


        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)

        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(addAll =false, file = "file1.txt file2.txt")
        git.status()

        val expectedOutput = "\nCurrent branch:master\n\n" +
                "Untracked files:\nfile3.txt\ndata 3\nfile4.txt\n" +
                "data 4\n" +
                "\nTracked files:\nfile1.txt\ndata 1\nfile2.txt\ndata 2\n"
        val actual = outputStream.toString().replace("\r\n", "\n")

        assertEquals(expectedOutput, actual)
    }


    @Test
    fun `Status after adding all blobs`() {

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val blob1=Blob(fileName = "file1.txt", data = "data 1")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(addAll =true)
        git.status()

        val expectedOutput = "\nCurrent branch:master\n\nTracked files:\nfile1.txt\ndata 1\n"
        val actual = outputStream.toString().replace("\r\n", "\n")

        assertEquals(expectedOutput, actual)
    }


    @Test
    fun `Add single blob`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(false, "file1.txt")

        val trackedFiles=git.getTrackedFiles()
        assertEquals(1, trackedFiles.size)
        assertEquals(blob1, trackedFiles[0])
    }
    @Test
    fun `Add all blobs`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(addAll = true)

        val trackedFiles=git.getTrackedFiles()
        assertEquals(4, trackedFiles.size)
        assertEquals(blobList, trackedFiles)
    }
    @Test
    fun `Add blob when not found`() {

        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        val exception = assertFailsWith<NotFoundException> {
            git.add(addAll = false, file = "notFound.txt")
        }
        val expectedOutput="Can't find file notFound.txt"
        assertEquals(expectedOutput, exception.message)

    }


    @Test
    fun `Get current node after initialization`() {

        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        val currentNode=git.getCurrentNode()
        val expectedOutput="master"
        assertEquals(expectedOutput,currentNode.getName())

    }

    @Test
    fun `Get current node after checkout`() {

        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        git.checkout(branchName = "main",create = true)
        val currentNode=git.getCurrentNode()
        val expectedOutput="main"
        assertEquals(expectedOutput,currentNode.getName())

    }

    @Test
    fun `Committing changes`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.checkout(branchName = "main",create = true)

        git.add(addAll = false, file = "file1.txt")

        val commitTime=Date()
        git.commit(Commit(author = "me", time = commitTime, message = "Added file1.txt", tree = git.getCurrentNode()))

        val commits=git.commitHistory().mapIndexed { index, commit ->
            "Commit ${index + 1}:\n"+
            "\tAuthor: ${commit.author}"+
            "\tMessage: ${commit.message}"+
            "\tCommit Time: ${commit.time}"
        }
        val expectedOutput="Commit 1:\n" +
                "\tAuthor: me" +
                "\tMessage: Added file1.txt" +
                "\tCommit Time: $commitTime"

        assertEquals(expectedOutput,commits.first())
    }

    @Test
    fun `Pushing changes`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(addAll = true)
        val commitTime=Date()
        git.commit(Commit(author = "me", time = commitTime, message = "Added all files", tree = git.getCurrentNode()))
        git.push()


        val untrackedFiles=git.getUntrackedFiles()
        val trackedFiles=git.getTrackedFiles()
        assertEquals(0, untrackedFiles.size)
        assertEquals(0, trackedFiles.size)
    }


    @Test
    fun `Pushing changes on same blob`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)

        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(addAll = true)
        var commitTime=Date()
        git.commit(Commit(author = "me", time = commitTime, message = "Added file1.txt", tree = git.getCurrentNode()))
        git.push()


        val blob2=Blob(fileName = "file1.txt", data = "data 2")
        blobList.remove(blob1)
        blobList.add(blob2)
        git.setUntrackedFiles(blobList)
        git.add(addAll = true)
        commitTime=Date()
        git.commit(Commit(author = "me", time = commitTime, message = "Updated file1.txt", tree = git.getCurrentNode()))
        git.push()

        val output=git.getCurrentNode().getBlobs().first()
        assertEquals("file1.txt",output.fileName)
        assertEquals("data 2",output.data)
    }

    @Test
    fun `Commit history retrieval`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(false,"file1.txt file2.txt")
        val commitTime=Date()
        git.commit(Commit(author = "me", time = commitTime, message = "Added file1 and file2", tree = git.getCurrentNode()))


        git.add(false,"file3.txt file4.txt")
        val commitTime2=Date()
        git.commit(Commit(author = "me", time = commitTime2, message = "Added file3 and file4", tree = git.getCurrentNode()))

        val output=git.commitHistory().mapIndexed { index, commit ->
            "\nCommit ${index + 1}:\n"+
            "\tAuthor: ${commit.author}\n"+
            "\tMessage: ${commit.message}\n"+
            "\tCommit Time: ${commit.time}\n"
        }

        val expectedOutput = "\nCommit 1:\n"+
                "\tAuthor: me\n" +
                "\tMessage: Added file1 and file2\n" +
                "\tCommit Time: ${commitTime}\n" +
                "\nCommit 2:\n" +
                "\tAuthor: me\n" +
                "\tMessage: Added file3 and file4\n" +
                "\tCommit Time: $commitTime2\n"

        assertEquals(expectedOutput, output.joinToString(""))



    }

    @Test
    fun `Find commit by Id`() {

        val blob1=Blob(fileName = "file1.txt", data = "data 1")
        val blob2=Blob(fileName = "file2.txt", data = "data 2")
        val blob3=Blob(fileName = "file3.txt", data = "data 3")
        val blob4=Blob(fileName = "file4.txt", data = "data 4")

        val blobList:MutableList<Blob> = mutableListOf()
        blobList.add(blob1)
        blobList.add(blob2)
        blobList.add(blob3)
        blobList.add(blob4)
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = blobList)

        git.add(false,"file1.txt")
        git.commit(Commit(author = "me", time =  Date(), message = "Added file1.txt", tree = git.getCurrentNode()))

        val expectedId = git.commitHistory()[0].getID()
        val actualId = git.findCommitById(expectedId).getID()

        assertEquals(expectedId, actualId)
    }


    @Test
    fun `Find commit by Id when not found`() {

        val id="0000"
        val tree=Tree(name = "master")
        val git=Git(tree = tree, current = tree, untrackedFiles = mutableListOf())
        val exception = assertFailsWith<NotFoundException> {
            git.findCommitById(id)
        }
        val expectedOutput="Commit with id $id not found"
        assertEquals(expectedOutput, exception.message)

    }
}