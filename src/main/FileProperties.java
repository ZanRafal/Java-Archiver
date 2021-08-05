package main;

public class FileProperties {
    private String name;
    private long size;
    private long compressedSize;
    private int compressionMethod;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public FileProperties(String name, long size, long compressedSize, int compressionMethod) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.compressionMethod = compressionMethod;
    }

    public long getCompressionRatio() {
        return 100 - ((compressedSize * 100) / size);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if(size > 0) {
            stringBuilder.append("\t");
            stringBuilder.append(size / 1024);
            stringBuilder.append(" KB (");
            stringBuilder.append(compressedSize / 1024);
            stringBuilder.append(" KB) compression: ");
            stringBuilder.append(getCompressionRatio());
            stringBuilder.append("%");
        }

        return stringBuilder.toString();
    }
}
