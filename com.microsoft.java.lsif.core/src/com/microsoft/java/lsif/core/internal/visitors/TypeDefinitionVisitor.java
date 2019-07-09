///* --------------------------------------------------------------------------------------------
// * Copyright (c) Microsoft Corporation. All rights reserved.
// * Licensed under the MIT License. See License.txt in the project root for license information.
// * ------------------------------------------------------------------------------------------ */
//
//package com.microsoft.java.lsif.core.internal.visitors;
//
//import java.util.List;
//
//import org.eclipse.core.runtime.NullProgressMonitor;
//import org.eclipse.jdt.core.dom.SimpleName;
//import org.eclipse.jdt.core.dom.SimpleType;
//import org.eclipse.jdt.ls.core.internal.JDTUtils;
//import org.eclipse.jdt.ls.core.internal.handlers.NavigateToTypeDefinitionHandler;
//import org.eclipse.lsp4j.Location;
//import org.eclipse.lsp4j.Position;
//import org.eclipse.lsp4j.TextDocumentIdentifier;
//import org.eclipse.lsp4j.TextDocumentPositionParams;
//
//import com.microsoft.java.lsif.core.internal.LanguageServerIndexerPlugin;
//import com.microsoft.java.lsif.core.internal.emitter.LsifEmitter;
//import com.microsoft.java.lsif.core.internal.indexer.IndexerContext;
//import com.microsoft.java.lsif.core.internal.indexer.LsifService;
//import com.microsoft.java.lsif.core.internal.indexer.Repository;
//import com.microsoft.java.lsif.core.internal.protocol.Document;
//import com.microsoft.java.lsif.core.internal.protocol.Range;
//import com.microsoft.java.lsif.core.internal.protocol.ResultSet;
//import com.microsoft.java.lsif.core.internal.protocol.TypeDefinitionResult;
//
//public class TypeDefinitionVisitor extends ProtocolVisitor {
//
//	public TypeDefinitionVisitor(LsifService lsif, IndexerContext context) {
//		super(lsif, context);
//	}
//
//	@Override
//	public boolean visit(SimpleName node) {
//		emitTypeDefinition(node.getStartPosition(), node.getLength());
//		return false;
//	}
//
//	@Override
//	public boolean visit(SimpleType node) {
//		emitTypeDefinition(node.getStartPosition(), node.getLength());
//		return false;
//	}
//
//	private void emitTypeDefinition(int startPosition, int length) {
//
//		try {
//			org.eclipse.lsp4j.Range fromRange = JDTUtils.toRange(this.getContext().getCompilationUnit().getTypeRoot(),
//					startPosition,
//					length);
//
//			if (fromRange == null) {
//				return;
//			}
//
//			Location targetLocation = computeTypeDefinitionNavigation(fromRange.getStart().getLine(),
//					fromRange.getStart().getCharacter());
//
//			if (targetLocation == null) {
//				return;
//			}
//
//			LsifService lsif = this.getLsif();
//			Document docVertex = this.getContext().getDocVertex();
//
//			// Definition start position
//			// Source range:
//			Range sourceRange = Repository.getInstance().enlistRange(lsif, docVertex, fromRange);
//
//			// Target range:
//			org.eclipse.lsp4j.Range toRange = targetLocation.getRange();
//			Document targetDocument = Repository.getInstance().enlistDocument(lsif,
//					targetLocation.getUri());
//			Range targetRange = Repository.getInstance().enlistRange(lsif, targetDocument, toRange);
//
//			// Result set
//			ResultSet resultSet = Repository.getInstance().enlistResultSet(lsif, sourceRange);
//
//			// Link resultSet & typeDefinitionResult
//			TypeDefinitionResult defResult = lsif.getVertexBuilder().typeDefinitionResult(targetRange.getId());
//			LsifEmitter.getInstance().emit(defResult);
//			LsifEmitter.getInstance().emit(lsif.getEdgeBuilder().typeDefinition(resultSet, defResult));
//		} catch (Throwable ex) {
//			LanguageServerIndexerPlugin.logException("Exception when dumping type definition information ", ex);
//		}
//	}
//
//	private Location computeTypeDefinitionNavigation(int line, int column) {
//		TextDocumentPositionParams documentSymbolParams = new TextDocumentPositionParams(
//				new TextDocumentIdentifier(this.getContext().getDocVertex().getUri()), new Position(line, column));
//		NavigateToTypeDefinitionHandler proxy = new NavigateToTypeDefinitionHandler();
//		List<? extends Location> typeDefinition = proxy.typeDefinition(documentSymbolParams, new NullProgressMonitor());
//		return typeDefinition != null && typeDefinition.size() > 0 ? typeDefinition.get(0) : null;
//	}
//}
