export function CheckForDuplicatesDataInArray(data: any, dataSource: Array<any>): boolean {
    if (dataSource.includes(data)) {
        return true;
    } else {
        return false;
    }
}


export function RemoveDuplicatesBetweenTwoArrays(newData: Array<any> | null, previousData: Array<any>): Array<any> {
    if (newData) {
        return newData.filter(element => !previousData.includes(element));
    }
    else {
        return previousData;
    }

}
