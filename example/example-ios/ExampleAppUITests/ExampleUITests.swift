import XCTest
import Cucumberish
import ExampleUITestShared

@objc public class ExampleUITests: NSObject {

    @objc public class func exampleUITestsInit() {
        var adapter: Kaluga_uitestDefaultApplicationAdapter? = nil

        beforeStart {
            Given(CounterTestCase.GivenAppLaunched.shared.definition.definitionString) { _, _ in
                adapter = CounterAdapterKt.createCounterAdapter()
                adapter!.launch(identifier: nil, arguments: [:])
            }

            When(CounterTestCase.WhenTapIncrement.shared.definition.definitionString) { _, _ in
                CounterScenariosKt.tapIncrementButton(adapter: adapter!)
            }

            Then(CounterTestCase.ThenCounterShows.shared.definition.definitionString) { args, _ in
                guard let value = args?[0] as? String else { return }
                do {
                    try CounterScenariosKt.assertCounterShows(adapter: adapter!, expected: value)
                } catch {
                    XCTFail(error.localizedDescription)
                }
            }
        }

        let bundle = Bundle(for: ExampleUITests.self)
        Cucumberish.executeFeatures(inDirectory: "Features", from: bundle, includeTags: nil, excludeTags: nil)
    }
}
