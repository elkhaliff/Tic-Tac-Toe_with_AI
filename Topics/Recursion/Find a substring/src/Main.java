class Util {
    public static int indexOf(String src, String tgt) {
        if (src.length() >= tgt.length()) {
            if (src.startsWith(tgt)) {
                return 0;
            } else {
                int index = indexOf(src.substring(1), tgt);
                return index < 0 ? -1 : 1 + index;
            }
        } else {
            return -1;
        }
    }
}