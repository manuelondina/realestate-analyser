#!/bin/bash

# Maven wrapper script for this project
# Uses project-specific settings.xml to avoid global Inditex repository configuration

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SETTINGS_FILE="${PROJECT_DIR}/.mvn/settings.xml"

# Check if project settings file exists
if [[ ! -f "$SETTINGS_FILE" ]]; then
    echo "❌ Error: Project settings.xml not found at $SETTINGS_FILE"
    exit 1
fi

echo "🔧 Using project-specific Maven settings: $SETTINGS_FILE"
echo "📦 Repositories: Maven Central + Spring Milestones"
echo ""

# Run Maven with project-specific settings
./mvnw --settings "$SETTINGS_FILE" "$@"