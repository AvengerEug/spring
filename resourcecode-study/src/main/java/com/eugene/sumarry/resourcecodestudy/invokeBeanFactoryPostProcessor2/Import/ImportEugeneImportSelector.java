package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.Import;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno.ImportEugene;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.postprocessor.ImportEugeneBeanFactoryProcessor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自定义的ImportSelector,
 * 实现一个功能: 若在导入类中添加@ImportEugene自定义注解时,
 * 在每个bean创建之前打印一句"========ImportEugene========"
 */
public class ImportEugeneImportSelector implements ImportSelector {

    /**
     *
     * @param importingClassMetadata 能获取到被导入的那个类的信息, 比如是在类A中使用
     *        @Import注解将当前类(MyImportSelector)引入, 那么importingClassMetadata
     *        中存的信息就是类A,
     *        若类A是一个注解, 那也能获取到使用注解A的类的信息(eg: 此例子中的AppConfig类)
     *
     * @return spring内部会将返回出来的字符串数组(要类的全类名)也当成bean
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (importingClassMetadata.hasAnnotation(ImportEugene.class.getName())) {
            return new String[] {
                    ImportEugeneBeanFactoryProcessor.class.getName()
            };
        }

        return new String[0];
    }
}
