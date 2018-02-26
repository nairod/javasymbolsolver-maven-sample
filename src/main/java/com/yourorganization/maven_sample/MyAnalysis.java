package com.yourorganization.maven_sample;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.MemoryTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class MyAnalysis {

	public static void main(String[] args) {
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver(false));
		combinedTypeSolver.add(new MemoryTypeSolver());
		combinedTypeSolver.add(new JavaParserTypeSolver(new File(
				"/home/dorian/dev/workspace2/javasymbolsolver-maven-sample/target/classes/com/yourorganization/maven_sample")));

		// Configure JavaParser to use type resolution
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
		ParserConfiguration symbolResolver = JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);

		Path path = Paths.get(
				"/home/dorian/dev/workspace2/javasymbolsolver-maven-sample/src/main/java/com/yourorganization/maven_sample");
		SourceRoot sourceRoot = new SourceRoot(path);
		sourceRoot.setParserConfiguration(symbolResolver);

		// Our sample is in the root of this directory, so no package name.
		CompilationUnit cu = sourceRoot.parse("", "FooHandler.java");

		cu.findAll(MethodCallExpr.class).forEach(be -> {
			ResolvedType resolvedType = be.calculateResolvedType();

			System.out.println(be.toString() + " is a: " + resolvedType);
			if (be.getNameAsString().equals("getBarExt") && resolvedType.asReferenceType().getQualifiedName().contains("IBarView")) {				
				be.setName("getBarView");
				be.addArgument("IBarView.class");
			}			
		});
		sourceRoot.saveAll(Paths.get("/home/dorian/dev/workspace2/javasymbolsolver-maven-sample/migrated"));
	}
}
