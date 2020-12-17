<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 分页处理 -->
<table width='100%' align='center' style='font-size: 13px;'
	class="yahoo">
	<tr>
		<td
			style='COLOR: #0061de; MARGIN-RIGHT: 3px; PADDING-TOP: 2px; TEXT-DECORATION: none'>
			总${pb.pageCount}页数&nbsp;&nbsp;总记录条数${pb.rowCount}
			<c:choose>
				<c:when test="${pb.pageNow==1}">
					<span class='disabled'>上一页</span>
					<c:forEach begin="1" end="${pb.pageCount}" var="i">
						<c:choose>
							<c:when test="${pb.pageNow==i}">
								<span class='current'>${i}</span>
							</c:when>
							<c:otherwise>
								<a href='#' onclick="toPage(${i})">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<span class='current'><a href='#'
						onclick="toPage(${pb.pageNow+1})">下一页</a></span>
				</c:when>
				<c:when test="${pb.pageNow==pb.pageCount}">
					<span class='current'><a href='#'
						onclick="toPage(${pb.pageNow-1})">上一页</a></span>
					<c:forEach begin="1" end="${pb.pageCount}" var="i">
						<c:choose>
							<c:when test="${pb.pageNow==i}">
								<span class='current'>${i}</span>
							</c:when>
							<c:otherwise>
								<a href='#' onclick="toPage(${i})">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<span class='disabled'>下一页</span>
				</c:when>
				<c:otherwise>
					<span class='current'><a href='#' onclick="toPage(${pb.pageNow-1})" >上一页</a></span>
					<c:forEach begin="1" end="${pb.pageCount}" var="i">
						<c:choose>
							<c:when test="${pb.pageNow==i}">
								<span class='current'>${i}</span>
							</c:when>
							<c:otherwise>
								<a href='#' onclick="toPage(${i})">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<span class='current'><a href='#'
						onclick="toPage(${pb.pageNow+1})">下一页</a></span>
				</c:otherwise>
			</c:choose> &nbsp;跳转到&nbsp;&nbsp;<input
			style='text-align: center; BORDER-RIGHT: #aaaadd 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #aaaadd 1px solid; PADDING-LEFT: 5px; PADDING-BOTTOM: 2px; MARGIN: 2px; BORDER-LEFT: #aaaadd 1px solid; COLOR: #000099; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaadd 1px solid; TEXT-DECORATION: none'
			type='text' size='2' id='pager_jump_page_size' value='${pb.pageNow }' /> &nbsp;<input
			type='button'
			style='text-align: center; BORDE R-RIGHT: #dedfde 1px solid; PADDING-RIGHT: 6px; BACKGROUND-POSITION: 50% bottom; BORDER-TOP: #dedfde 1px solid; PADDING-LEFT: 6px; PADDING-BOTTOM: 2px; BORDER-LEFT: #dedfde 1px solid; COLOR: #0061de; MARGIN-RIGHT: 3px; PADDING-TOP: 2px; BORDER-BOTTOM: #dedfde 1px solid; TEXT-DECORATION: none'
			value='确定' id='pager_jump_btn' onclick="pagerJump()"/>
		</td>
	</tr>
</table>