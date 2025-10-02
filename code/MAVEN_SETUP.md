# Maven Configuration

This project uses project-specific Maven settings to ensure consistent builds across different environments.

## 🔧 Setup

The project includes:

1. **Project-specific Maven settings** (`.mvn/settings.xml`)
   - Uses Maven Central and Spring Milestones repositories
   - Self-contained configuration

2. **Convenient build script** (`mvn-local.sh`)
   - Automatically uses project-specific settings
   - Shows which repositories are being used

## 🚀 Usage

### Use the local script (Recommended)
```bash
# Build the project
./mvn-local.sh clean compile

# Run tests
./mvn-local.sh test

# Package application
./mvn-local.sh package

# Install to local repository
./mvn-local.sh install
```

### Use Maven wrapper directly
```bash
# With project settings
./mvnw --settings .mvn/settings.xml clean compile

# Standard wrapper
./mvnw clean compile
```

## 📦 Multi-Module DDD Structure

```
realestate-analyser/
├── domain/          # Pure business logic
├── application/     # Use cases & application services
├── infrastructure/  # JPA, databases, external APIs
└── api/            # REST controllers & Spring Boot app
```

### Module Dependencies
- **Domain**: No dependencies (pure Java)
- **Application**: Depends on domain only
- **Infrastructure**: Depends on domain + application
- **API**: Depends on all modules (Spring Boot entry point)

## ✅ Verification

When running commands, you should see:
```
🔧 Using project-specific Maven settings
📦 Repositories: Maven Central + Spring Milestones

Downloading from central: https://repo1.maven.org/maven2/...
```

This confirms the correct repositories are being used.