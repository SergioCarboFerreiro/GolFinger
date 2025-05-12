//
//  AppDelegate.swift
//  iosApp
//
//  Created by Sergio Ferreiro on 12/05/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        print("✅ FirebaseApp configured")
        return true
    }
}
