# How to Commit Project to GitHub

This guide will help you commit your Android project to GitHub.

---

## 📋 Prerequisites

1. **GitHub Account**: Make sure you have a GitHub account
2. **Git Installed**: Git should be installed on your system
3. **GitHub Repository**: Create a new repository on GitHub (or use existing one)

---

## 🚀 Step-by-Step Guide

### Step 1: Create GitHub Repository

1. Go to [GitHub](https://github.com)
2. Click the **"+"** icon → **"New repository"**
3. Enter repository name (e.g., `clothing-shopping-app`)
4. Choose **Public** or **Private**
5. **DO NOT** initialize with README, .gitignore, or license (we already have these)
6. Click **"Create repository"**

### Step 2: Add All Files to Git

```bash
# Add all files to staging
git add .

# Check what will be committed
git status
```

### Step 3: Create Initial Commit

```bash
# Create your first commit
git commit -m "Initial commit: Clothing Shopping App with Firebase integration"
```

### Step 4: Connect to GitHub Repository

```bash
# Add your GitHub repository as remote (replace with your repo URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Verify remote was added
git remote -v
```

### Step 5: Push to GitHub

```bash
# Push to GitHub (first time)
git push -u origin master

# Or if your default branch is 'main'
git push -u origin main
```

---

## 🔐 Authentication

### Option 1: Personal Access Token (Recommended)

1. Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click **"Generate new token"**
3. Select scopes: `repo` (full control)
4. Copy the token
5. When prompted for password, paste the token instead

### Option 2: SSH Key

```bash
# Generate SSH key (if you don't have one)
ssh-keygen -t ed25519 -C "your_email@example.com"

# Add SSH key to GitHub
# Copy the public key: cat ~/.ssh/id_ed25519.pub
# Add it to GitHub → Settings → SSH and GPG keys

# Use SSH URL instead
git remote set-url origin git@github.com:YOUR_USERNAME/YOUR_REPO_NAME.git
```

---

## 📝 Complete Command Sequence

Here's the complete sequence of commands:

```bash
# 1. Check current status
git status

# 2. Add all files
git add .

# 3. Create commit
git commit -m "Initial commit: Clothing Shopping App with Firebase integration"

# 4. Add remote repository (replace with your URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# 5. Push to GitHub
git push -u origin master
```

---

## ⚠️ Important Notes

### Files Included/Excluded

**✅ Included:**
- All source code (Kotlin files)
- Resources (drawables, layouts, etc.)
- Gradle files
- Documentation (README.md, etc.)
- `google-services.json` (Firebase config - safe to commit)

**❌ Excluded (via .gitignore):**
- Build files (`/build`)
- Local properties (`local.properties`)
- IDE files (`.idea/`)
- Keystore files (`*.jks`, `*.keystore`)
- Generated files

### Security Considerations

1. **google-services.json**: 
   - ✅ Safe to commit (contains project ID, not secrets)
   - This file is needed for Firebase to work

2. **Keystore Files**:
   - ❌ Never commit keystore files (`.jks`, `.keystore`)
   - Already excluded in `.gitignore`

3. **API Keys**:
   - If you have sensitive API keys, use environment variables
   - Or use `local.properties` (already in `.gitignore`)

---

## 🔄 Future Commits

After the initial commit, use these commands for future updates:

```bash
# Check what changed
git status

# Add specific files
git add app/src/main/java/com/example/app_ui/screens/HomeScreen.kt

# Or add all changes
git add .

# Commit with descriptive message
git commit -m "Add new feature: Product search functionality"

# Push to GitHub
git push
```

---

## 📦 Commit Message Best Practices

Write clear, descriptive commit messages:

```bash
# Good examples:
git commit -m "Add user authentication with Firebase"
git commit -m "Fix cart item image display issue"
git commit -m "Update UI: Implement dark mode support"
git commit -m "Refactor: Move API calls to repository layer"

# Bad examples:
git commit -m "fix"
git commit -m "update"
git commit -m "changes"
```

---

## 🌿 Branch Management

### Create a New Branch

```bash
# Create and switch to new branch
git checkout -b feature/new-feature

# Make changes, then commit
git add .
git commit -m "Add new feature"

# Push new branch to GitHub
git push -u origin feature/new-feature
```

### Switch Branches

```bash
# Switch to master/main
git checkout master

# Switch to another branch
git checkout feature/new-feature
```

---

## 🔍 Useful Git Commands

```bash
# View commit history
git log

# View changes in a file
git diff app/src/main/java/com/example/app_ui/screens/HomeScreen.kt

# Undo changes (before commit)
git checkout -- app/src/main/java/com/example/app_ui/screens/HomeScreen.kt

# View remote repositories
git remote -v

# Update from remote
git pull origin master
```

---

## 🐛 Troubleshooting

### Error: "remote origin already exists"

```bash
# Remove existing remote
git remote remove origin

# Add new remote
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
```

### Error: "failed to push some refs"

```bash
# Pull remote changes first
git pull origin master --allow-unrelated-histories

# Then push
git push -u origin master
```

### Error: Authentication failed

- Use Personal Access Token instead of password
- Or set up SSH keys
- Check your GitHub username is correct

---

## 📚 Repository Structure on GitHub

After pushing, your GitHub repository will have:

```
clothing-shopping-app/
├── .gitignore
├── README.md
├── FIREBASE_*.md (documentation files)
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

---

## ✅ Verification

After pushing, verify on GitHub:

1. Go to your repository on GitHub
2. Check that all files are present
3. Verify `README.md` displays correctly
4. Check that code is visible and formatted properly

---

## 🎯 Quick Reference

| Action | Command |
|--------|---------|
| **Check status** | `git status` |
| **Add files** | `git add .` |
| **Commit** | `git commit -m "message"` |
| **Add remote** | `git remote add origin <URL>` |
| **Push** | `git push -u origin master` |
| **Pull** | `git pull origin master` |
| **View history** | `git log` |
| **Create branch** | `git checkout -b <branch-name>` |

---

## 🔗 Next Steps

1. ✅ Commit your code to GitHub
2. 📝 Update README.md with project description
3. 🏷️ Add tags/releases for version control
4. 🤝 Add collaborators (if needed)
5. 📊 Set up GitHub Actions for CI/CD (optional)

---

**Need Help?**
- GitHub Docs: https://docs.github.com
- Git Documentation: https://git-scm.com/doc
- GitHub Support: https://support.github.com

