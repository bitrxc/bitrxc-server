/*******************************************************************************
 * Copyright (C) 2014 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package cn.edu.bit.ruixin.doc.writer.simple;

import static cn.edu.bit.ruixin.doc.util.AnnotationUtils.getAnnotationName;

import com.sun.javadoc.*;
import cn.edu.bit.ruixin.doc.Configuration;
import cn.edu.bit.ruixin.doc.model.ClassDescriptor;
import cn.edu.bit.ruixin.doc.model.Endpoint;
import cn.edu.bit.ruixin.doc.model.PathVar;
import cn.edu.bit.ruixin.doc.model.QueryParam;
import cn.edu.bit.ruixin.doc.writer.swagger.model.Parameter;
import cn.edu.bit.ruixin.doc.collector.spring.SpringCollector;
import sun.reflect.generics.tree.TypeArgument;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static cn.edu.bit.ruixin.doc.util.CommonUtils.*;

public class SimpleHtmlWriter implements cn.edu.bit.ruixin.doc.writer.Writer
{
    public static final String OUTPUT_OPTION_NAME = "legacy";
    private static final String DEFAULT_STYLESHEET = "default-stylesheet.css";

    //scy
    private static HashMap<String, String> type_name_2_type_inf = new HashMap<String, String>();

    static
    {
        type_name_2_type_inf.put("Integer", "int");
        type_name_2_type_inf.put("String", "string");
//        type_name_2_type_inf.put("List","[]");
//        type_name_2_type_inf.put("Map","Map");
        type_name_2_type_inf.put("Boolean", "boolean");
        type_name_2_type_inf.put("Object", "object");
        type_name_2_type_inf.put("MultipartFile", "文件");
        type_name_2_type_inf.put("Void", "null");


    }


    //scy
    private static void print_String(String s)
    {
        System.out.println(s);
    }

    private static final String tab = " &nbsp ";

    private static String type_2_json_inf_String(com.sun.javadoc.Type type, int level)//level：该type位于0,1,2……层级。tab数正比于level
    {
        return type_2_json_inf_String(type, level, null, null);
    }

    //scy
    private static String type_2_json_inf_String(com.sun.javadoc.Type type, int level, TypeVariable[] type_Vars, com.sun.javadoc.Type[] type_Arguments)//level：该type位于0,1,2……层级。tab数正比于level
    {
        print_String(",,,type_name：" + type.typeName() + "。level=" + level);
        if ("<any>".equals(type.typeName()))
        {
            print_String(",,,----------<any>is coming");
            print_String("qualifiedTypeName:" + type.qualifiedTypeName());
            print_String("simpleTypeName:" + type.simpleTypeName());
            ParameterizedType parameterized_type = type.asParameterizedType();
            if (parameterized_type == null)
            {
                print_String("parameterized_type==null");
            }
            else
            {
                com.sun.javadoc.Type[] types = parameterized_type.typeArguments();
                if (types.length > 0)
                {
                    for (int i = 0; i < types.length; i++)
                    {
                        print_String(",,,types[i].typeName():" + types[i].typeName());
                    }
                }
                else
                {
                    print_String(",,,types.length<=0");
                }
            }
            print_String(",,,-----------<any>over");

            return "<any>";
        }


        if (type == null)
        {
            return "type==null";
        }
        if (level > 10)
        {
            return "level大于5";
        }

//        System.out.println("***type_2_json_inf_String："+" 【level="+level+"】 "+"type.typeName()="+type.typeName());
        if (type.isPrimitive())
        {
            print_String("111 isPRi");
            return type.typeName();
        }
        String type_name = type.typeName();
        if (type_name_2_type_inf.get(type_name) != null)
        {
            return type_name_2_type_inf.get(type_name);
        }

        if ("List".equals(type_name))
        {
            ParameterizedType parameterized_type = type.asParameterizedType();
            if (parameterized_type == null)
            {
                return "[|fail(            parameterized_type==null)|]";

            }
            com.sun.javadoc.Type[] types = parameterized_type.typeArguments();


            if (types.length <= 0)
            {
                return "[|fail(types.length<=0)|]";

            }
            String tabs =level>0? String.format("%0" + (level + 0) + "d", 0).replace("0", tab):"";
            String tabs2 = String.format("%0" + (level + 1) + "d", 0).replace("0", tab);

            String ret = "[\n" + tabs2;
            for (com.sun.javadoc.Type tmp_type : types)
            {
                ret += type_2_json_inf_String(tmp_type, level + 1, type_Vars, type_Arguments);
                ret += " ,\n";
            }
            return ret + tabs + " ]";


//            return "[ "+  type_2_json_inf_String(type.getElementType(),0)  +",  ]";//only for 原生数组(List不算)
        }
        if ("Map".equals(type_name))
        {
            ParameterizedType parameterized_type = type.asParameterizedType();
            if (parameterized_type == null)
            {
                return "Map|fail(            parameterized_type==null)|]";

            }
            com.sun.javadoc.Type[] types = parameterized_type.typeArguments();


            if (types.length == 2)
            {
                String ret = "{" + tab;
                ret += type_2_json_inf_String(types[0], level + 1, type_Vars, type_Arguments);
                ret += tab + " : " + tab;
                ret += type_2_json_inf_String(types[1], level + 1, type_Vars, type_Arguments);

//                return ret + "," + " }";
                return ret +  " }";

            }

            else
            {
                return "Map|fail(types.length!=2)|]";

            }


        }




        TypeVariable typeVar = type.asTypeVariable();
        if (typeVar != null)
        {
            String s_typeVar = typeVar.typeName();
            print_String("typename：" + type_name + "也有" + "type.asTypeVariable()" + "s_typeVar：" + s_typeVar + "\n---BEGIN---type_Vars[]为：");

            for (int i = 0; i < type_Vars.length; i++)
            {
                print_String(type_Vars[i].typeName());
                if (s_typeVar.equals(type_Vars[i].typeName()))
                {
                    return type_2_json_inf_String(type_Arguments[i], level , null, null);
                }
            }
            print_String("___END___");
        }


        ParameterizedType parameterized_type = type.asParameterizedType();
        if (parameterized_type != null)
        {
            print_String("typename：" + type_name + "也有" + "type.asParameterizedType()");


            com.sun.javadoc.Type[] type_arguments = parameterized_type.typeArguments();

            if (type_arguments!=null  && type_arguments.length > 0)
            {
//                boolean is_same = true;
//                if (type_Arguments!=null  &&  type_arguments.length == type_Arguments.length)
//                {
//                    for (int i = 0; i < type_arguments.length; i++)
//                    {
//                        if (type_arguments[i].equals(type_Arguments[i])==false)
//                        {
//                            is_same = false;
//                            break;
//                        }
//                    }
//                }
//                else
//                {
//                    is_same = false;
//                }
//                if (is_same == false)

                for (int i = 0; i < type_arguments.length; i++)
                {
                    print_String(",,,types[i].typeName():" + type_arguments[i].typeName());
//                    ret+=type_2_json_inf_String(type_Arguments[i],0);
                    if (type_arguments[i].asTypeVariable() != null)
                    {
                        TypeVariable type_argument_as_typeVar = type_arguments[i].asTypeVariable();
                        String s_typeVar = type_argument_as_typeVar.typeName();
//                        print_String("typename："+type_name+"也有"+"type.asTypeVariable()"+"s_typeVar："+s_typeVar+"\n---BEGIN---type_Vars[]为：");

                        for (int j = 0; j < type_Vars.length; j++)
                        {
                            print_String(type_Vars[j].typeName());
                            if (s_typeVar.equals(type_Vars[j].typeName()))
                            {
                                type_arguments[i] = type_Arguments[j];
//                                return type_2_json_inf_String(type_Arguments[i],level+1,null,null);
                            }
                        }
//                        print_String("___END___");
                    }
                }
                ClassDoc class_doc = type.asClassDoc();
                TypeVariable[] type_vars = class_doc.typeParameters();

//                return type_2_json_inf_String(type, level + 1, type_vars, type_arguments);
                type_Vars=type_vars;
                type_Arguments=type_arguments;


            }
            else
            {
                print_String(",,,types.length<=0 or ==null");
//                ret+="!=null但,,,types.length<=0";

            }
        }


        String tabs = String.format("%0" + (level + 1) + "d", 0).replace("0", tab);
        String tabs2 = "";
        if (level > 0)
        {
            tabs2 = String.format("%0" + (level) + "d", 0).replace("0", tab);

        }

//        print_String("tabs"+tabs);
        String ret = "";
        ClassDoc classDoc = type.asClassDoc();
        if (classDoc == null)
        {
            System.out.println("***" + " 【level=" + level + "】 " + "type.asClassDoc()==null。type.typeName()=" + type.typeName());
            return "";
        }

        for (FieldDoc fd : classDoc.fields())
        {
            System.out.println("***" + " 【level=" + level + "】 " + "FieldDoc  " + fd.name());
            if ("Constructor".equals(fd.type().typeName()))
            {
//                System.out.println("  "+" 【level="+level+"】 "+" this fd is Constructor");
                continue;
            }
            ret += tabs + "\"" + fd.name() + "\" : " + type_2_json_inf_String(fd.type(), level + 1, type_Vars, type_Arguments) + " ,\n";
        }
        if (ret.length() > 2)
        {
            return "{\n" + ret.substring(0, ret.length() - 2) + "\n" + tabs2 + "}";

        }
        else
        {
            print_String("空，type_name=" + type_name + ";  type.toString()=" + type.toString() + ";  type.asClassDoc().name()=" + type.asClassDoc().name());
            return "空，type_name=" + type_name;
        }
    }

    @Override
    public void write(Collection<ClassDescriptor> classDescriptors, Configuration config) throws IOException
    {

        if (config.isdefaultStyleSheet())
        {
            generateStyleSheet(config);
        }

        writeHtml(classDescriptors, config);
    }

    private static void generateStyleSheet(Configuration config) throws IOException
    {
        InputStream in = null;
        OutputStream out = null;
        try
        {

            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_STYLESHEET);
            out = new FileOutputStream(new File(config.getStyleSheet()));

            copy(in, out);

        }
        finally
        {
            close(in, out);
        }
    }

    private static String parameters_to_string(com.sun.javadoc.Parameter[] parameters)
    {
        String ret = "(";
        for (com.sun.javadoc.Parameter para : parameters)
        {
            ret = ret + para.typeName() + " &nbsp  " + para.name() + " , ";
        }
        return ret + ")";
    }

    private static void writeHtml(Collection<ClassDescriptor> classDescriptors, Configuration config) throws IOException
    {

        PrintWriter out = null;
        //scy
        int endPoint_ct = 0;
//        String[] css_bg_color={"lightgoldenrodyellow","#d3cad9","lightcyan","#f1c4cd"};
        String[] css_bg_color = {"#fffff6", "#f6ffff", "#fff6ff"};

        try
        {
            out = new PrintWriter(new File(".", "index.html"), "UTF-8");

            out.println("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            out.println("    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");

            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");


            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"utf-8\"\" />");
            out.println("<title>" + config.getDocumentTitle() + "</title>");
            out.println("<link rel='stylesheet' type='text/css' href=' " + config.getStyleSheet() + "'/>");
            out.println("</head>");

            out.println("<body>");

            out.println("<div id=\"wrapper\">");
            out.println("<div id=\"container\">");

            out.println("<h1>" + config.getDocumentTitle() + "</h1>");

//            out.println("<hr style=\"border: 7px solid blue;\"/>");

//            System.out.println("classDescriptors长度：|||"+classDescriptors.size());
            for (ClassDescriptor classDescriptor : classDescriptors)
            {
                out.println("<div id='" + classDescriptor.getName().replace(" ", "_") + "' class=\"whole_class\" >");
                out.printf("<div class=\"class_Head\">");


                out.println("<h3>" + "类名：" + classDescriptor.getName() + "</h3>");
                out.println("<h5>" + "&nbsp &nbsp -ContextPath：" + classDescriptor.getContextPath() + "</h5>");
                out.println("<h5>" + "&nbsp &nbsp -Description：" + classDescriptor.getDescription() + "</h5>");
                out.printf("</div>");

//                out.print("<div class=\"bean_description\">" + classDescriptor.getDescription() + "</div>");

                for (Endpoint endpoint : classDescriptor.getEndpoints())
                {

                    out.println("<div class=\"endPoint_wrapper\"  style=\"background-color: " + css_bg_color[(endPoint_ct++) % css_bg_color.length] + ";\">");
                    out.println("<div class=\"line_between_EndPoint\"></div>");


                    out.printf("<div class=\"EndPoint_Head_0\"><span>方法名：</span><span>%s</span></div>", endpoint.getMethodDoc().name());
                    out.printf("<div class=\"EndPoint_Head_1\"><span>-参数：</span><span>%s</span></div>", parameters_to_string(endpoint.getMethodDoc().parameters()));
                    out.printf("<div class=\"EndPoint_Head_1\"><span>-返回值：</span><span>return &nbsp %s &nbsp &nbsp &nbsp  ;</span></div>", endpoint.getMethodDoc().returnType());


                    out.printf("<div class=\"EndPoint_Head_1\"><span>-对应的前后端接口：</span><span>&nbsp  &nbsp  &nbsp &nbsp &nbsp  </span></div>");

                    out.println("<div class=\"div_endpoint\">");

                    out.println("<div class=\"info_title\">请求方法(Method)</div>");
                    out.println("<div class=\"info_text\">" + endpoint.getHttpMethod() + "</div>");


                    out.println("<div class=\"info_title\">请求路径(Path)</div>");
                    out.println("<div class=\"info_text\">" + endpoint.getPath() + "</div>");


                    if (!isEmpty(endpoint.getPathVars()))
                    {

                        out.println("<div class=\"info_title\">路径变量(Path Variables)</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        for (PathVar pathVar : endpoint.getPathVars())
                        {
//                            out.println("<tr>");
//                            out.println("<td class=\"code_format\">" + pathVar.getName() + "</td>");
//                            out.println("<td class=\"descr_format\">" + pathVar.getDescription() + "</td>");
//                            out.println("</tr>");


                            //scy
                            out.println("<tr>");
                            out.println("<td class=\"code_format\">" + "<span class=\"var_name\">" + pathVar.getName() + "</span>" + "</td>");
                            out.println("<td class=\"descr_format\">" + type_2_json_inf_String(pathVar.getType(), 0).replace("\n", "<br>") + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }

                    if (!isEmpty(endpoint.getQueryParams()))
                    {

                        out.println("<div class=\"info_title\">查询参数(Query Parameters)</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        for (QueryParam queryParam : endpoint.getQueryParams())
                        {
                            out.println("<tr>");
                            out.println("<td class=\"code_format\">" + "<span class=\"var_name\">" + queryParam.getName() + "</span>" + (queryParam.isRequired() ? " (required)" : "") + "</td>");
//                            out.println("<td class=\"descr_format\">" + queryParam.getDescription() + "</td>");
                            out.println("<td class=\"descr_format\">" + type_2_json_inf_String(queryParam.getType(), 0).replace("\n", "<br>") + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }


//                    if (endpoint.getRequestBody() != null &&
//                            !isEmpty(endpoint.getRequestBody().getDescription())) {
                    if (endpoint.getRequestBody() != null)
                    {
                        out.println("<div class=\"info_title\">请求体(Request Body)</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        out.println("<tr>");
                        out.println("<td class=\"code_format\">" + "<span class=\"var_name\">" + endpoint.getRequestBody().getName() + "</span>" + "</td>");

                        out.println("<td class=\"descr_format\">" + type_2_json_inf_String(endpoint.getRequestBody().getType(), 0).replace("\n", "<br>") + "</td>");

                        out.println("</tr>");
                        out.println("</table>");
                    }


                    //response body
                    boolean has_response_body = false;
                    AnnotationDesc[] method_annotations = endpoint.getMethodDoc().annotations();
                    for (AnnotationDesc anno : method_annotations)
                    {
                        if (SpringCollector.RESPONSEBODY_ANNOTATION.equals(getAnnotationName(anno)))
                        {
                            has_response_body = true;
                        }
                    }
                    if (has_response_body == false)
                    {
                        AnnotationDesc[] class_annotations = endpoint.getClassDoc().annotations();
                        for (AnnotationDesc anno : class_annotations)
                        {
                            if (SpringCollector.RESTCONTROLLER_ANNOTATION.equals(getAnnotationName(anno)))
                            {
                                has_response_body = true;
                            }
                        }
                    }
                    if (has_response_body)
                    {
                        out.println("<div class=\"info_title\">响应体(Response Body)</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        out.println("<tr>");
//                        out.println("<td class=\"code_format\">" + "<span class=\"var_name\">"+endpoint.getRequestBody().getName() +"</span>"+ "</td>");
                        print_String(",,,,响应体");
                        out.println("<td class=\"descr_format\">" + type_2_json_inf_String(endpoint.getType(), 0).replace("<", " &lt; ").replace(">", " &gt; ").replace("\n", "<br>") + "</td>");

                        out.println("</tr>");
                        out.println("</table>");
                    }

                    if (!isEmpty(endpoint.getConsumes()))
                    {
                        out.println("<div class=\"info_title\">Consumes</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        for (String acceptType : endpoint.getConsumes())
                        {
                            out.println("<tr>");
                            out.println("<td class=\"code_format\">" + acceptType + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }

                    if (!isEmpty(endpoint.getProduces()))
                    {
                        out.println("<div class=\"info_title\">Produces</div>");
                        out.println("<table width=\"100%\" class=\"list\">");
                        for (String outputType : endpoint.getProduces())
                        {
                            out.println("<tr>");
                            out.println("<td class=\"code_format\">" + outputType + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }

                    out.println("<div class=\"info_title\">注释</div>");
//                    System.out.println("+++<div class=\"info_text\">" + endpoint.getDescription() + "</div>");
                    out.println("<div class=\"info_text\">" + endpoint.getDescription() + "</div>");


                    out.println("</div>");
                    out.println("</div>");

                }

                out.println("</div>");
//                out.println("<hr />");


            }

            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        }
        finally
        {
            close(out);
        }
    }


}
