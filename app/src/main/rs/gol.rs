#pragma version(1)
#pragma rs java_package_name(com.github.eagraf.mobileworker)

uint8_t RS_KERNEL gol(uint8_t in) {
    uint8_t out;
    if (in == 0) {
        out = 1;
    } else {
        out = 0;
    }
    return out;
}
