package cn.edu.bit.ruixin.doc;

import com.sun.javadoc.*;

/**
 * doclet示例类
 * 本项目采用jdk8，因此包名为{@link com.sun.javadoc}
 */
public class Docgen extends Doclet {
    public static boolean start(RootDoc rootDoc) {
        System.out.println("Hello my doclet");
        for (ClassDoc classDoc : rootDoc.classes()) {
            System.out.println("ClassName:" + classDoc.qualifiedName());
        }
        return true;
    }
}