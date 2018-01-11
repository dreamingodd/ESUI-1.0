function resizeOuterDivHeight() {
    var chartDivs = $('.chartDiv');
    var height = 400;
    height = height + (chartDivs.length - 1) / 2 * 400;
    $('.outer').css('height', height);
}