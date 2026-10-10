package cn.kio.cloud.boot.db.autoconfigure;

/**
 * @author kio
 */
public interface DynamicDataSourcePropertiesCustomizer {

    /**
     * Customize the given a {@link DynamicDataSourceProperties} object.
     *
     * @param properties the com.chc.dmp.boot.db.autoconfigure.DynamicDataSourceProperties object to customize
     */
    void customize(DynamicDataSourceProperties properties);
}
