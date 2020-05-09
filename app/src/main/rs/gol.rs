#pragma version(1)
#pragma rs java_package_name(com.github.eagraf.mobileworker)

rs_allocation board;
int32_t size;

uint8_t RS_KERNEL gol(uint8_t in, uint32_t x) {

    uint32_t index = x;
    uint32_t xc = x % size;
    uint32_t yc = x / size;

    uint32_t x1 = (size + xc - 1) % size;
    uint32_t x2 = xc;
    uint32_t x3 = (size + xc + 1) % size;

    uint32_t y1 = ((size + yc - 1) % size) * size;
    uint32_t y2 = yc * size;
    uint32_t y3 = ((size + yc + 1) % size) * size;

    uint8_t count = 0;

    count += rsGetElementAt_uchar(board, x1 + y1);
    count += rsGetElementAt_uchar(board, x2 + y1);
    count += rsGetElementAt_uchar(board, x3 + y1);

    count += rsGetElementAt_uchar(board, x1 + y2);
    count += rsGetElementAt_uchar(board, x3 + y2);

    count += rsGetElementAt_uchar(board, x1 + y3);
    count += rsGetElementAt_uchar(board, x2 + y3);
    count += rsGetElementAt_uchar(board, x3 + y3);

    uint8_t out;
    if (in == 0) {
        if (count == 3) {
            out = 1;
        } else {
            out = 0;
        }
    } else {
        if (count < 2) {
            out = 0;
        } else if (count > 3) {
            out = 0;
        } else {
            out = 1;
        }
    }
    return out;
}
