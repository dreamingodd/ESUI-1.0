function getArraySize(arr) {
    var len = 0;
    for (var o in arr) {
        len++;
    }
    return len;
}