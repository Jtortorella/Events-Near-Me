
class DebuggerService {
    private componentLocation: string;

    constructor(inputtedComponentLocation: string) {
        this.componentLocation = inputtedComponentLocation;
    }

    showError = (error?: Error, errorText?: string ) : void => {
         console.log(`We've recieved the following error in the ${this.componentLocation}`);
         console.log(errorText)
         console.log(`${this.componentLocation}`);
         console.error(error);
    }

    showVerbose = (data?: any): void => {
        console.log(`We've recieved the following data in the ${this.componentLocation}`);
        console.log(data);
    }

}

export default DebuggerService

