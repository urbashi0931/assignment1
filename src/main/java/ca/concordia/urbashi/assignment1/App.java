package ca.concordia.urbashi.assignment1;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import me.tomassetti.support.DirExplorer;

/**
 * This class contains the main method for running the code. It has three main
 * methods for checking hashcode and equals bug, Duplicate logging information
 * in catch clause and also detecting useless control flow
 * 
 * @author Urbashi Bhattacharjee
 * 
 * 
 */

public class App {
	public static int hashCode_line = -1;
	public static int equals_line = -1;

	public static void main(String[] args) {
		// checkHashCodeAndEquals();
	/*	
		
		BugDetector bugDetector = new BugDetector();
		List<String> errorListForMissingMethod = bugDetector
				.missingEqualMethodDetector(new File("sampleTestFiles\\equalsHashcodeHandler"));

		List<String> errorListForDuplicateLogging = bugDetector
				.getDuplicateLoggingErrors(new File("sampleTestFiles\\InadequateLoggingInformationHandler"));

		for (String error : errorListForMissingMethod) {
			System.out.println(error);
		}

		for(String error:errorListForDuplicateLogging) { System.out.println(error); }
	*/	 

		checkDuplicateLoggingInformationinCatchBlocks();
		//checkHashCodeAndEquals();
		//checkUselessControlFlow();
		// checkDuplicateLoggingInformationinCatchBlocks();
	}

	/**
	 * checkHashCodeAndEquals()explores ClouStack and report bug if any
	 */
	public static void checkHashCodeAndEquals() {

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
						public void visit(MethodDeclaration n, Object arg) {
							super.visit(n, arg);

							NodeList<Parameter> nodes = n.getParameters();
							if (n.getNameAsString().equals("equals") && n.getTypeAsString().equals("boolean")
									&& (nodes.size() == 1) && nodes.get(0).getTypeAsString().equals("Object")) {

								App.equals_line = n.getRange().isPresent() ? n.getRange().get().begin.line : 0;

							}

							if (n.getNameAsString().equals("hashCode") && n.getTypeAsString().equals("int")
									&& (nodes.size() == 0)) {
								// System.out.println("Found hashCode");
								App.hashCode_line = n.getRange().isPresent() ? n.getRange().get().begin.line : 0;
							}

						}

					}.visit(JavaParser.parse(file), null);

					if (hashCode_line > -1 && equals_line == -1)
						System.err.println("\tViolate the invariant equal objects must have equal hashcodes on line: "
								+ hashCode_line);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).explore(new File("C:\\Users\\USER\\eclipse-workspace\\assignmentFinal\\assignment1\\cloudstack-4.9"));

	}

	/**
	 * 
	 * @method checkDuplicateLoggingInformationinCatchBlocks() explores ClouStack
	 *         and report bug if any
	 */

	public static void checkDuplicateLoggingInformationinCatchBlocks() {

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
											System.out.println(methodCallExpr);
											if (methodCallExpr.getArguments().size() == 1
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
										System.err.print("Duplicate error log on: ");
										for (String s : intersection) {
											System.err.println(bugLineMap.get(s));
										}
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
		}).explore(new File("C:\\Users\\USER\\eclipse-workspace\\assignmentFinal\\assignment1\\cloudstack-4.9"));

	}

	/**
	 * @method checkUselessControlFlow() explores ClouStack and report bug if any.
	 *         It takes into account empty For Each/Switch/if/if...else/while
	 *         loop/for loop/Do...while loop and else-if ladder block
	 */

	public static void checkUselessControlFlow() {

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
						public void visit(ForeachStmt n, Object arg) {
							// TODO Auto-generated method stub
							super.visit(n, arg);
							Statement bs = n.getBody();
							if (bs.isBlockStmt() && ((BlockStmt) bs).getStatements().isEmpty()) {
								// System.out.println("Bug found on line: " + n.getRange().get().begin.line);
								printErrorLine(n);
							}
						}

						@Override
						public void visit(WhileStmt n, Object arg) {
							// TODO Auto-generated method stub
							super.visit(n, arg);
							Statement bs = n.getBody();
							if (bs.isBlockStmt() && ((BlockStmt) bs).getStatements().isEmpty()) {
								// System.out.println("Bug found on line: " + n.getRange().get().begin.line);
								printErrorLine(n);
							}
						}

						@Override
						public void visit(DoStmt n, Object arg) {
							// TODO Auto-generated method stub
							super.visit(n, arg);
							Statement bs = n.getBody();
							if (bs.isBlockStmt() && ((BlockStmt) bs).getStatements().isEmpty()) {
								// System.out.println("Bug found on line: " + n.getRange().get().begin.line);
								printErrorLine(n);
							}
						}

						@Override
						public void visit(ForStmt n, Object arg) {
							// TODO Auto-generated method stub
							super.visit(n, arg);
							Statement bs = n.getBody();
							if (bs.isBlockStmt() && ((BlockStmt) bs).getStatements().isEmpty()) {
								// System.out.println("Bug found on line: " + n.getRange().get().begin.line);
								printErrorLine(n);
							}
						}

						@Override
						public void visit(IfStmt n, Object arg) {
							super.visit(n, arg);
							boolean elsePresent = false;
							if (n.getElseStmt().isPresent() && n.getElseStmt().get() instanceof BlockStmt) {
								elsePresent = !((BlockStmt) n.getElseStmt().get()).isEmpty();
							}

							if (n.getThenStmt().isEmptyStmt() && !elsePresent) {
								printErrorLine(n);
							}
						}

						public void printEmptyStmet(Node node) {
							if (node instanceof BlockStmt) {
								if (((BlockStmt) node).getStatements().isEmpty()) {
									printErrorLine(node);
								}
							} else if (node instanceof SwitchEntryStmt) {
								if (((SwitchEntryStmt) node).getStatements().isEmpty()) {
									printErrorLine(node);
								} else if (node instanceof ForStmt) {
									if (((ForStmt) node).getBody().isEmptyStmt()) {
										printErrorLine(node);
									} else {
										NodeList<Statement> stmts = ((SwitchEntryStmt) node).getStatements();
										for (Statement stmt : stmts) {
											printEmptyStmet(stmt);
										}
									}

								}
							}
						}

						public void printErrorLine(Node node) {
							System.err.println("Error at Line Number : "
									+ (node.getRange().isPresent() ? node.getRange().get().begin.line : 0));
						}

						@Override
						public void visit(SwitchStmt n, Object arg) {
							// TODO Auto-generated method stub
							super.visit(n, arg);
							Stack<Node> nodeofIf = new Stack<Node>();
							nodeofIf.addAll(n.getEntries());
							if (nodeofIf.isEmpty()) {
								printErrorLine(n);
							}
							while (!nodeofIf.empty()) {
								Node node = nodeofIf.pop();
								this.printEmptyStmet(node);
							}
						}

					}.visit(JavaParser.parse(file), null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).explore(new File("C:\\Users\\USER\\eclipse-workspace\\assignmentFinal\\assignment1\\cloudstack-4.9"));

	}
}
