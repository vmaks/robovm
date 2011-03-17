/*
 * Copyright (C) 2011 The NullVM Open Source Project
 *
 * TODO: Insert proper license header.
 */
package org.nullvm.compiler.llvm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 *
 * @version $Id$
 */
public class Module {
    private final List<URL> includes = new ArrayList<URL>();
    private final List<Global> globals = new ArrayList<Global>();
    private final List<Function> functions = new ArrayList<Function>();
    private final List<UserType> types = new ArrayList<UserType>();

    public void addInclude(URL resource) {
        includes.add(resource);
    }
    
    public Function newFunction(String name, FunctionType type, String ... parameterNames) {
        Function f = new Function(name, type, parameterNames);
        functions.add(f);
        return f;
    }
    
    public void addGlobal(Global global) {
        globals.add(global);
    }
    
    public void addType(UserType type) {
        types.add(type);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (URL g : includes) {
            InputStream in = null;
            try {
                in = g.openStream();
                sb.append(IOUtils.toString(in, "UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
            sb.append("\n");
        }
        for (UserType type : types) {
            sb.append(type.getAlias());
            sb.append(" = type ");
            sb.append(type.getDefinition());
            sb.append("\n");
        }
        for (Global g : globals) {
            sb.append(g.getDefinition());
            sb.append("\n");
        }
        if (!globals.isEmpty() && !functions.isEmpty()) {
            sb.append("\n");
        }
        for (Function f : functions) {
            sb.append(f.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}