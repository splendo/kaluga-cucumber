#import <Foundation/Foundation.h>
#import "ExampleAppUITests-Swift.h"

__attribute__((constructor))
void ExampleUITestsInit(void)
{
    [ExampleUITests exampleUITestsInit];
}
