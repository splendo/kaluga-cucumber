import SwiftUI

struct ContentView: View {
    @State private var count = 0

    var body: some View {
        VStack(spacing: 16) {
            Text("\(count)")
                .accessibilityIdentifier("counter_value")
            Button("Increment") {
                count += 1
            }
            .accessibilityIdentifier("increment_button")
        }
        .padding()
    }
}
