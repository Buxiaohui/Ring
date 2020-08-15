/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.apt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.buxiaohui.annotation.LightNaviService;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

@AutoService(Processor.class)
public class MyClass extends AbstractProcessor {
    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;
    private Elements elementUtils;
    private Map<String, List<Element>> annotationClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        System.out.println("--process");
        TypeSpec autoClass = TypeSpec.classBuilder("AutoClassTest").build();

        JavaFile javaFile = JavaFile.builder("com.baidu.BaiduMap.lightservice", autoClass).build();

        try {
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            System.out.println("--process,error:" + e);
            e.printStackTrace();
        }

        return true;

    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(LightNaviService.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror,
                                                         ExecutableElement executableElement, String s) {
        return super.getCompletions(element, annotationMirror, executableElement, s);
    }

    @Override
    protected synchronized boolean isInitialized() {
        return super.isInitialized();
    }

    /**
     * 打印log
     *
     * @param msg  信息
     * @param args 参数
     */
    private void printLog(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    /**
     * 打印log
     *
     * @param msg 信息
     */
    private void printLog(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}