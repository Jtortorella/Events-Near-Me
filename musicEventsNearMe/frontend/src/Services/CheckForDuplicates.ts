export function CheckForDuplicatesDataInArray(data: any, dataSource: Array<any>): boolean {
    if (dataSource.includes(data)) {
        return true;
    } else {
        return false;
    }
}


export function RemoveDuplicatesBetweenTwoArrays(data: Array<any>, dataSource: Array<any>): Array<any> {
    return data.filter(element => !dataSource.includes(element));
}
