package ca.concordia.urbashi.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import me.tomassetti.support.DirExplorer;

public class BugDetector {
	 
	 
public List<String> missingEqualMethodDetector(File rootDir){
	
	 
	final List<String> errorList=new ArrayList<String>();
	
	//from App Main
	
	new DirExplorer(new DirExplorer.Filter() {
		public boolean interested(int level, String path, File file) {
			return path.endsWith(".java") || file.getName().endsWith(".java");
		}
	}, new DirExplorer.FileHandler() {
		public void handle(int level, String path, File file) {
			System.out.println("Checking: " + path + " ...");
			
			final int[] hashcode_errorline= {-1};
			final int[] equals_errorline= {-1};
			
			try {
				new VoidVisitorAdapter<Object>() {

					@Override
					public void visit(MethodDeclaration n, Object arg) {
						super.visit(n, arg);

						NodeList<Parameter> nodes = n.getParameters();
						if (n.getNameAsString().equals("equals") && n.getTypeAsString().equals("boolean")
								&& (nodes.size() == 1) && nodes.get(0).getTypeAsString().equals("Object")) {

					
							equals_errorline[0] = n.getRange().isPresent() ? n.getRange().get().begin.line : 0;

						}

						if (n.getNameAsString().equals("hashCode") && n.getTypeAsString().equals("int")
								&& (nodes.size() == 0)) {
							 hashcode_errorline[0] = n.getRange().isPresent() ? n.getRange().get().begin.line : 0;
						}

					}

				}.visit(JavaParser.parse(file), null);
	
				if (hashcode_errorline[0] > -1 && equals_errorline[0] == -1) {
				
				errorList.add("\tViolate the invariant equal objects must have equal hashcodes on line: "
							+ hashcode_errorline[0]);
				}	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}).explore(rootDir);
	
	
	return errorList;
	
	
}


public List<String> getDuplicateLoggingErrors(File rootDirectory) {
	
	final List<String> errorList=new ArrayList<String>();
	
	new DirExplorer(new DirExplorer.Filter() {
		public boolean interested(int level, String path, File file) {
			return path.endsWith(".java");
		}
	}, new DirExplorer.FileHandler() {
		public void handle(int level, String path, File file) {
			System.out.println("Checking: " + path + " ...");

			try {
				new VoidVisitorAdapter<Object>() {

					@Override
					public void visit(TryStmt n, Object arg) {
						super.visit(n, arg);

						ArrayList<Set<String>> logExpressionList = new ArrayList<Set<String>>();
						HashMap<String, ArrayList<Integer>> bugLineMap = new HashMap<String, ArrayList<Integer>>();

						for (CatchClause catchClause : n.getCatchClauses()) {
							Stack<Node> nodeStack = new Stack<Node>();
							Set<String> logExpressions = new HashSet<String>();

							nodeStack.addAll(catchClause.getChildNodes());

							while (!nodeStack.empty()) {
								Node node = nodeStack.pop();

								// System.out.println("Node:");
								// System.out.println(node.toString());

								if (node instanceof BlockStmt) {

									nodeStack.addAll(((BlockStmt) node).getChildNodes());

									for (Statement statement : ((BlockStmt) node).getStatements()) {
										nodeStack.addAll(statement.getChildNodes());
									}

								} else if (node instanceof MethodCallExpr) {

									MethodCallExpr methodCallExpr = ((MethodCallExpr) node);
									if (methodCallExpr.getNameAsString().matches("warn|println|info|debug|error")) {
										if (methodCallExpr.getArguments().size() ==1
												&& methodCallExpr.getArgument(0) instanceof StringLiteralExpr) {

											String logArg = methodCallExpr.getArgument(0).toString();

											logExpressions.add(logArg);

											ArrayList<Integer> lines = bugLineMap.get(logArg);

											if (lines == null) {
												lines = new ArrayList<Integer>();
												bugLineMap.put(logArg, lines);
											}

											lines.add(methodCallExpr.getRange().isPresent()
													? methodCallExpr.getRange().get().begin.line
													: 0);
										}

									}
								}

							}

							logExpressionList.add(logExpressions);

						}

						for (Set<String> logExpressions_i : logExpressionList) {
							for (Set<String> logExpressions_j : logExpressionList) {

								if (logExpressions_i == logExpressions_j)
									continue;

								Set<String> intersection = new HashSet<String>(logExpressions_i);

								intersection.retainAll(logExpressions_j);

								if (intersection.size() > 0) {
									String errorMessage = "Duplicate error log on: ";
									
									for (String s : intersection) {
										errorMessage += bugLineMap.get(s) + "; ";
									}
									
									errorList.add(errorMessage);
								}

							}
						}

					}
				}.visit(JavaParser.parse(file), null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}).explore(rootDirectory);
	
	
	
	return errorList;
}

}
