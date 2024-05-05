import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
       KoinHelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
