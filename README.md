# GitApp

## Usage

### 1. Initializing a Git Repository

    To start a new Git repository, use the following command:

```bash
    git init
```

### 2. Selecting specific branch

    If you want to work on a particular branch, use either of the following commands:

    If the branch already exists:

```bash
   git checkout main
```

    If the branch doesn't exist:

```bash
   git checkout -b main
```

### 3. Checking the Status

    To check the status of your changes, use:

```bash
 git status
```

### 4. Adding Files to Staging Area

    You can add changes using the following commands:

    Add all changes:

```bash
    git add .
```

    Add a specific file:

```bash
   git add file1.txt
```

    Add multiple files:

```bash
  git add file1.txt file2.txt file3.txt
```

### 5. Committing Changes

    After staging your changes, commit them with a message

```bash
   git commit -m "message"
```

### 6. Pushing Changes

    To push your committed changes:

```bash
    git push
```

### 7. Viewing Commit History

    To see the commit history for the current branch:

    ⚠️ Note: This command doesn't show the detailed commit data!

```bash
    git history
```

### 8. Finding a Specific Commit

    If you need to find information about a specific commit, use:

    ⚠️ Note: This command prints the detailed data of the commit!

```bash
    git find commitId
```

#### For example:

```bash
    git find cfcdd5e8ffe5d77a1da18f3c365a94efc2039ae9
```

### 9. Viewing All Branches

    For a visual representation of all branches :

```bash
   git show
```

### 10. Viewing All Commands

    To see a list of available Git commands:

```bash
   git
```
