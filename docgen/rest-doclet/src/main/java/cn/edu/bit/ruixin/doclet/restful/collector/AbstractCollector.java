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
package cn.edu.bit.ruixin.doclet.restful.collector;


import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import cn.edu.bit.ruixin.doclet.restful.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import static java.util.Collections.emptyList;
import static cn.edu.bit.ruixin.doclet.restful.util.CommonUtils.*;
import static cn.edu.bit.ruixin.doclet.restful.util.TagUtils.*;

public abstract class AbstractCollector implements Collector {

    protected abstract boolean shouldIgnoreClass(ClassDoc classDoc);

    protected abstract boolean shouldIgnoreMethod(MethodDoc methodDoc);

    protected abstract EndpointMapping getEndpointMapping(ProgramElementDoc doc);

    protected abstract Collection<PathVar> generatePathVars(MethodDoc methodDoc);

    protected abstract Collection<QueryParam> generateQueryParams(MethodDoc methodDoc);

    protected abstract RequestBody generateRequestBody(MethodDoc methodDoc);

    /**
     * Will generate and aggregate all the rest endpoint class descriptors.
     *
     * @param rootDoc
     * @return
     */
    @Override
    public Collection<ClassDescriptor> getDescriptors(RootDoc rootDoc) {
        Collection<ClassDescriptor> classDescriptors = new ArrayList<ClassDescriptor>();


        //Loop through all of the classes and if it contains endpoints then add it to the set of descriptors.
        for (ClassDoc classDoc : rootDoc.classes()) {
            System.out.println("\n\n\n______________\n+++" + classDoc.name());

            ClassDescriptor descriptor = getClassDescriptor(classDoc);
            if (descriptor == null)
                System.out.println("case1   descriptor is null+++");
            else
            {
                if (!isEmpty(descriptor.getEndpoints()))
                    System.out.println("case2   isEmpty+++");
            }


            if (descriptor != null && !isEmpty(descriptor.getEndpoints())) {
                System.out.println("YES " + classDoc.name() + "descriptor != null && !isEmpty(descriptor.getEndpoints())|||" + classDoc.name());

                classDescriptors.add(descriptor);
            } else {
                System.out.println("NULL " + classDoc.name() + "descriptor != null && !isEmpty(descriptor.getEndpoints())|||" + classDoc.name());
            }
        }

        return classDescriptors;
    }

    /**
     * Will generate a single class descriptor and all the endpoints for that class.
     * <p>
     * If any class contains the special javadoc tag {@link cn.edu.bit.ruixin.doclet.restful.util.TagUtils.IGNORE_TAG} it will be excluded.
     *
     * @param classDoc
     * @return
     */
    protected ClassDescriptor getClassDescriptor(ClassDoc classDoc) {
        System.out.println("+++" + 0);
        //If the ignore tag is present or this type of class should be ignored then simply ignore this class









        if (!isEmpty(classDoc.tags(IGNORE_TAG)) || shouldIgnoreClass(classDoc))
            return null;
        System.out.println("+++" + 1);

        String contextPath = getContextPath(classDoc);
        Collection<Endpoint> endpoints = getAllEndpoints(contextPath, classDoc, getEndpointMapping(classDoc));

        //If there are no endpoints then no use in providing documentation.
        if (isEmpty(endpoints))
        {
            System.out.println(classDoc.name()+"isEmpty(endpoints)++++");
            return null;
        }
        System.out.println("+++" + 2);

        String name = getClassName(classDoc);
        String description = getClassDescription(classDoc);

        return new ClassDescriptor(
                (name == null ? "" : name),
                (contextPath == null ? "" : contextPath),
                endpoints,
                (description == null ? "" : description)
        );
    }

    /**
     * Retrieves all the end point provided in the specified class doc.
     *
     * @param contextPath
     * @param classDoc
     * @param classMapping
     * @return
     */
    protected Collection<Endpoint> getAllEndpoints(String contextPath, ClassDoc classDoc, EndpointMapping classMapping) {
        Collection<Endpoint> endpoints = new ArrayList<Endpoint>();

        for (MethodDoc method : classDoc.methods(true))
            endpoints.addAll(getEndpoint(contextPath, classMapping, method,classDoc));

        //Check super classes for inherited methods
        if (classDoc.superclass() != null)
            endpoints.addAll(getAllEndpoints(contextPath, classDoc.superclass(), classMapping));

        return endpoints;
    }

    /**
     * Retrieves the endpoint for a single method.
     * <p>
     * If any method contains the special javadoc tag {@link cn.edu.bit.ruixin.doclet.restful.util.TagUtils.IGNORE_TAG} it will be excluded.
     *
     * @param contextPath
     * @param classMapping
     * @param method
     * @return
     */
//    protected Collection<Endpoint> getEndpoint(String contextPath, EndpointMapping classMapping, MethodDoc method) {
    //scy
    protected Collection<Endpoint> getEndpoint(String contextPath, EndpointMapping classMapping, MethodDoc method,ClassDoc classDoc) {

        //If the ignore tag is present then simply return nothing for this endpoint.
        if (!isEmpty(method.tags(IGNORE_TAG)) || shouldIgnoreMethod(method))
            return emptyList();

        Collection<Endpoint> endpoints = new ArrayList<Endpoint>();
        EndpointMapping methodMapping = getEndpointMapping(method);

        Collection<String> paths = resolvePaths(contextPath, classMapping, methodMapping);
        Collection<String> httpMethods = resolveHttpMethods(classMapping, methodMapping);
        Collection<String> consumes = resolveConsumesInfo(classMapping, methodMapping);
        Collection<String> produces = resolvesProducesInfo(classMapping, methodMapping);
        Collection<PathVar> pathVars = generatePathVars(method);
        Collection<QueryParam> queryParams = generateQueryParams(method);
        RequestBody requestBody = generateRequestBody(method);

        for (String httpMethod : httpMethods)
            for (String path : paths)
                endpoints.add(
                        new Endpoint(
                                path,
                                httpMethod,
                                queryParams,
                                pathVars,
                                requestBody,
                                consumes,
                                produces,
                                method.commentText(),
                                firstSentence(method),
                                //scy
                                method,
                                classDoc
                        )
                );

        return endpoints;
    }

    /**
     * Will get the initial context path to use for all rest endpoint.
     * <p>
     * This looks for the value in a special javadoc tag {@link cn.edu.bit.ruixin.doclet.restful.util.TagUtils.CONTEXT_TAG}
     *
     * @param classDoc
     * @return
     */
    protected String getContextPath(ClassDoc classDoc) {
        if (!isEmpty(classDoc.tags(CONTEXT_TAG)))
            return classDoc.tags(CONTEXT_TAG)[0].text();

        return "";
    }

    /**
     * Will get the display name for the class.
     * <p>
     * This looks for the value in a special javadoc tag {@link cn.edu.bit.ruixin.doclet.restful.util.TagUtils.NAME_TAG}
     *
     * @param classDoc
     * @return
     */
    protected String getClassName(ClassDoc classDoc) {
        if (!isEmpty(classDoc.tags(NAME_TAG))) {


//            我的代码
            System.out.println("  classDoc.tags(NAME_TAG) ||| ");
            try {
                System.out.println(classDoc.tags(NAME_TAG));
            } catch (Exception e) {
                System.out.println(" 打印err ");
            }

            return classDoc.tags(NAME_TAG)[0].text();
        }

        return classDoc.typeName();
    }

    /**
     * Will get the description for the class.
     *
     * @param classDoc
     * @return
     */
    protected String getClassDescription(ClassDoc classDoc) {
        return classDoc.commentText();
    }

    /**
     * Will generate all the paths specified in the class and method mappings.
     * Each path should start with the context path, followed by one of the class paths,
     * then finally the method path.
     *
     * @param contextPath
     * @param classMapping
     * @param methodMapping
     * @return
     */
    protected Collection<String> resolvePaths(String contextPath, EndpointMapping classMapping, EndpointMapping methodMapping) {

        contextPath = (contextPath == null ? "" : contextPath);

        //Build all the paths based on the class level, plus the method extensions.
        LinkedHashSet<String> paths = new LinkedHashSet<String>();

        if (isEmpty(classMapping.getPaths())) {

            for (String path : methodMapping.getPaths())
                paths.add(fixPath(contextPath + path));

        } else if (isEmpty(methodMapping.getPaths())) {

            for (String path : classMapping.getPaths())
                paths.add(fixPath(contextPath + path));

        } else {

            for (String defaultPath : classMapping.getPaths())
                for (String path : methodMapping.getPaths())
                    paths.add(fixPath(contextPath + defaultPath + path));

        }

        return paths;
    }

    /**
     * Will use the method's mapped information if it is not empty, otherwise it will use the class mapping information
     * to retrieve all the https methods.
     *
     * @param classMapping
     * @param methodMapping
     * @return
     */
    protected Collection<String> resolveHttpMethods(EndpointMapping classMapping, EndpointMapping methodMapping) {
        return firstNonEmpty(
                methodMapping.getHttpMethods(),
                classMapping.getHttpMethods()
        );
    }

    /**
     * Will use the method's mapped information if it is not empty, otherwise it will use the class mapping information
     * to retrieve all the consumeable information.
     *
     * @param classMapping
     * @param methodMapping
     * @return
     */
    protected Collection<String> resolveConsumesInfo(EndpointMapping classMapping, EndpointMapping methodMapping) {
        return firstNonEmpty(
                methodMapping.getConsumes(),
                classMapping.getConsumes()
        );
    }

    /**
     * Will use the method's mapped information if it is not empty, otherwise it will use the class mapping information
     * to retrieve all the produceable information.
     *
     * @param classMapping
     * @param methodMapping
     * @return
     */
    protected Collection<String> resolvesProducesInfo(EndpointMapping classMapping, EndpointMapping methodMapping) {
        return firstNonEmpty(
                methodMapping.getProduces(),
                classMapping.getProduces()
        );
    }
}
