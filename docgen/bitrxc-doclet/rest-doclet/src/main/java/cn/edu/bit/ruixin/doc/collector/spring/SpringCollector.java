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
package cn.edu.bit.ruixin.doc.collector.spring;


import com.sun.javadoc.*;
import cn.edu.bit.ruixin.doc.collector.AbstractCollector;
import cn.edu.bit.ruixin.doc.collector.EndpointMapping;
import cn.edu.bit.ruixin.doc.model.PathVar;
import cn.edu.bit.ruixin.doc.model.QueryParam;
import cn.edu.bit.ruixin.doc.model.RequestBody;

import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static cn.edu.bit.ruixin.doc.util.AnnotationUtils.getAnnotationName;
import static cn.edu.bit.ruixin.doc.util.AnnotationUtils.getElementValue;
import static cn.edu.bit.ruixin.doc.util.CommonUtils.firstNonEmpty;
import static cn.edu.bit.ruixin.doc.util.CommonUtils.isEmpty;
import static cn.edu.bit.ruixin.doc.util.TagUtils.*;

public class SpringCollector extends AbstractCollector {

    protected static final List<String> CONTROLLER_ANNOTATION = Arrays.asList("org.springframework.stereotype.Controller",
                                                                        "org.springframework.web.bind.annotation.RestController");


    protected static final List<String> MAPPING_ANNOTATION=Arrays.asList( "org.springframework.web.bind.annotation.RequestMapping");
    protected static final List<String> REST_MAPPING_ANNOTATION=Arrays.asList( "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.DeleteMapping");
    protected static final String PATHVAR_ANNOTATION = "org.springframework.web.bind.annotation.PathVariable";
    protected static final String PARAM_ANNOTATION = "org.springframework.web.bind.annotation.RequestParam";
    protected static final String REQUESTBODY_ANNOTATION = "org.springframework.web.bind.annotation.RequestBody";
    //scy
    public static final String RESTCONTROLLER_ANNOTATION = "org.springframework.web.bind.annotation.RestController";
    public static final String RESPONSEBODY_ANNOTATION = "org.springframework.web.bind.annotation.ResponseBody";

    @Override
    protected boolean shouldIgnoreClass(ClassDoc classDoc) {
        System.out.println("\n\n\n________\nclassDoc:"+classDoc.name());
        //If found a controller annotation then don't ignore this class.
        for (AnnotationDesc classAnnotation : classDoc.annotations())
        {
            System.out.println("getAnnotationName(classAnnotation)+++"+getAnnotationName(classAnnotation));
            if (CONTROLLER_ANNOTATION.contains(getAnnotationName(classAnnotation))){
//                System.out.println("此anno包含在CONTROLLER_ANNOTATION中");
                return false;
            }
        }

        

        //If not found then ignore this class.
        return true;
    }

    @Override
    protected boolean shouldIgnoreMethod(MethodDoc methodDoc) {
        //If found a mapping annotation then don't ignore this class.
        for (AnnotationDesc classAnnotation : methodDoc.annotations())
//            if (MAPPING_ANNOTATION.contains(getAnnotationName(classAnnotation)))
            //scy
            if (MAPPING_ANNOTATION.contains(getAnnotationName(classAnnotation)) || REST_MAPPING_ANNOTATION.contains(getAnnotationName(classAnnotation)))
                return false;

        //If not found then ignore this class.
        return true;
    }

    @Override
    protected EndpointMapping getEndpointMapping(ProgramElementDoc doc) {
        //scy
        for (AnnotationDesc annotation : doc.annotations()) {
            //If found then extract the value (paths) and the methods.
            String annotationName=getAnnotationName(annotation);
            if (REST_MAPPING_ANNOTATION.contains(annotationName)) {

                //Get http methods from annotation
                Collection<String> httpMethods = new LinkedHashSet<String>();
                String httpMethod=annotationName.substring(annotationName.lastIndexOf(".") + 1);
                httpMethod=httpMethod.substring(0,httpMethod.indexOf("Mapping"));
                System.out.println("httpMethod："+httpMethod);
                httpMethods.add(httpMethod);




                return new EndpointMapping(
                        new LinkedHashSet<String>(getElementValue(annotation, "value")),
                        httpMethods,
                        new LinkedHashSet<String>(getElementValue(annotation, "consumes")),
                        new LinkedHashSet<String>(getElementValue(annotation, "produces"))
                );
            }

            if (MAPPING_ANNOTATION.contains(getAnnotationName(annotation))) {

                //Get http methods from annotation
                Collection<String> httpMethods = new LinkedHashSet<String>();
                for (String value : getElementValue(annotation, "method"))
                    httpMethods.add(value.substring(value.lastIndexOf(".") + 1));

                return new EndpointMapping(
                        new LinkedHashSet<String>(getElementValue(annotation, "value")),
                        httpMethods,
                        new LinkedHashSet<String>(getElementValue(annotation, "consumes")),
                        new LinkedHashSet<String>(getElementValue(annotation, "produces"))
                );
            }
        }

        //Simply return an empty grouping if no request mapping was found.
        return new EndpointMapping(
                Collections.<String>emptySet(),
                Collections.<String>emptySet(),
                Collections.<String>emptySet(),
                Collections.<String>emptySet()
        );
    }

    @Override
    protected Collection<PathVar> generatePathVars(MethodDoc methodDoc) {
        Collection<PathVar> retVal = new ArrayList<PathVar>();

        Tag[] tags = methodDoc.tags(PATHVAR_TAG);
        ParamTag[] paramTags = methodDoc.paramTags();

        for (Parameter parameter : methodDoc.parameters()) {
            for (AnnotationDesc annotation : parameter.annotations()) {
                if (getAnnotationName(annotation).equals(PATHVAR_ANNOTATION)) {
                    String name = parameter.name();
                    Collection<String> values = getElementValue(annotation, "value");
                    if (!values.isEmpty())
                        name = values.iterator().next();

                    //first check for special tag, then check regular param tag, finally default to empty string
                    String text = findParamText(tags, name);
                    if (text == null)
                        text = findParamText(paramTags, parameter.name());
                    if (text == null)
                        text = "";

                    retVal.add(new PathVar(name, text, parameter.type()));
                }
            }
        }

        return retVal;
    }

    @Override
    protected Collection<QueryParam> generateQueryParams(MethodDoc methodDoc) {
        Collection<QueryParam> retVal = new ArrayList<QueryParam> ();

        Tag[] tags = methodDoc.tags(QUERYPARAM_TAG);
        ParamTag[] paramTags = methodDoc.paramTags();

        for (Parameter parameter : methodDoc.parameters()) {
            for (AnnotationDesc annotation : parameter.annotations()) {
                if (getAnnotationName(annotation).equals(PARAM_ANNOTATION)) {
                    String name = parameter.name();
                    List<String> values = getElementValue(annotation, "value");
                    if (!values.isEmpty())
                        name = values.get(0);

                    List<String> requiredVals = getElementValue(annotation, "required");

                    //With spring query params are required by default
                    boolean required = TRUE;
                    if(!requiredVals.isEmpty())
                        required = Boolean.parseBoolean(requiredVals.get(0));

                    //With spring, if defaultValue is provided then "required" is set to false automatically
                    List<String> defaultVals = getElementValue(annotation, "defaultValue");

                    if (!defaultVals.isEmpty())
                        required = FALSE;

                    //first check for special tag, then check regular param tag, finally default to empty string
                    String text = findParamText(tags, name);
                    if (text == null)
                        text = findParamText(paramTags, parameter.name());
                    if (text == null)
                        text = "";

                    retVal.add(new QueryParam(name, required, text, parameter.type()));
                }
            }
        }
        return retVal;
    }

    @Override
    protected RequestBody generateRequestBody(MethodDoc methodDoc) {

        Tag[] tags = methodDoc.tags(REQUESTBODY_TAG);
        ParamTag[] paramTags = methodDoc.paramTags();

        for (Parameter parameter : methodDoc.parameters()) {
            for (AnnotationDesc annotation : parameter.annotations()) {
                if (getAnnotationName(annotation).equals(REQUESTBODY_ANNOTATION)) {

                    //first check for special tag, then check regular param tag, finally default to empty string
                    String text = (isEmpty(tags) ? null : tags[0].text());
                    if (text == null)
                        text = findParamText(paramTags, parameter.name());
                    if (text == null)
                        text = "";

                    return new RequestBody(parameter.name(), text, parameter.type());
                }
            }
        }
        return null;
    }

    @Override
    protected Collection<String> resolveHttpMethods(EndpointMapping classMapping, EndpointMapping methodMapping) {
        //If there are no http methods defined simply use GET
//        return firstNonEmpty(super.resolveHttpMethods(classMapping, methodMapping), asList("GET"));
        //scy
        return firstNonEmpty(super.resolveHttpMethods(classMapping, methodMapping), asList("NO_METHOD"));
    }
}
