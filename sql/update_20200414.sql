ALTER TABLE MON_HARDWARE_DISKINFO_DTL RENAME COLUMN "CHAR_BAK_2" TO "MOUNTED_ON";
COMMENT ON COLUMN MON_HARDWARE_DISKINFO_DTL.MOUNTED_ON IS '文件系统挂载点';
COMMENT ON COLUMN MON_HARDWARE_DISKINFO_DTL.DISK_AVAIL_SIZE IS '磁盘可用容量';