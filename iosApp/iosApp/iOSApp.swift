import SwiftUI
import UserNotifications

@main
struct iOSApp: App {
    init() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound]) { _, _ in }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
