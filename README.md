# Android CI/CD Pipeline

This repository contains a GitHub Actions workflow that automates the build and release process for an Android application. The workflow compiles the application, generates APK and AAB files, and creates a new GitHub release with these files attached. It is triggered by tagging a commit with a specific pattern (`release/*`).

## Workflow Overview

The `.github/workflows/android-build-and-release.yml` file defines the CI/CD pipeline. It performs the following actions:

1. Checks out the code.
2. Sets up the JDK environment.
3. Builds the APK and AAB files using Gradle.
4. Creates a GitHub release.
5. Attaches the APK and AAB files to the GitHub release.

## Prerequisites

- Android Studio project set up with Gradle.
- JDK 21 configured in the project.
- A `release` branch or appropriate branch strategy to initiate the release process.

## Usage

To trigger the workflow and create a new release:

1. Ensure your changes are merged into the `main` or `release` branch (as per your project's branching strategy).
2. Tag the commit you want to release with a `release/*` pattern, e.g., `release/1.0.0`.
```
git tag release/1.0.0
git push origin release/1.0.0
```

3. The workflow will trigger automatically, building the application and creating a new release named after the tag, with the APK and AAB files attached.

## Configuration

You may need to adjust the `android-build-and-release.yml` workflow file to match your project's specific requirements, such as changing the JDK version or modifying the Gradle build tasks.

## Personal Access Token (PAT)

This workflow uses a Personal Access Token (PAT) with `repo` scope for creating releases. Store your PAT in the repository's secrets with the name `PAT_FOR_RELEASES`.

## Contributing

Contributions to this project are welcome! Please fork the repository, make your changes, and submit a pull request.

## License

Apache 2.0

## Acknowledgments

- GitHub Actions for providing the automation platform.
- All contributors who help maintain and improve this project.

## Support

For support or questions about using this workflow, please [open an issue](https://github.com/ryanw-mobile/testlab-release-ci/issues) in this repository.
