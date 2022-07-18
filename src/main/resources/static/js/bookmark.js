var bookmarkNum
var isUserExists

function popOpen(recruit) {
    if (!isUserExists) {
        alert("로그인이 필요합니다.")
        window.location.href = "/login"
    }
    console.log(recruit);
    bookmarkNum = recruit

    $('#bookmark-modal').modal('show');

    getCategoryList()
}

function popClose() {
    $('#bookmark-modal').modal('hide');
}

function getCategoryList() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: "text",
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    });
}

function createCategory() {

    var name = $("#add-input").val();

    var newCategoryRequestDto = {
        name: name
    }

    $.ajax({
        type: "POST",
        url: "/category",
        data: newCategoryRequestDto
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    }).fail(function (xhr, status) {
        console.log(xhr)
        alert(xhr.responseJSON["message"])
    });

    rollbackModalFooter()
}

function rollbackModalFooter() {
    let addButton = '<button id="add-category-button" class="btn btn-primary" onclick=addCategory()>카테고리 추가</button>' +
        '<button id="bookmark-button" class="btn btn-primary" onclick=bookmarkRecruit()>북마크 추가</button>'
    $("#bookmark-footer").html(addButton)
}

function addCategory() {
    let addCategory = '<div id="add-category">' +
        '<input type="text" id="add-input">' +
        '<button class="btn btn-secondary" onclick="rollbackModalFooter()">취소</button>' +
        '<button id="add-category" class="btn btn-primary" onclick="createCategory()">저장</button>' +
        '</div>'
    $("#bookmark-footer").html(addCategory)
}

function deleteCategory(deleteId) {
    $.ajax({
        url: `/my-page/bookmark/?categoryId=${deleteId}`,
        type: "DELETE",
    }).done(function (fragment) {
        alert("삭제되었습니다.")
        $("#categoryList").replaceWith(fragment);
    }).fail(function(data) {
        alert("삭제에 실패했습니다.")
        console.log(data)
    });
}

function bookmarkRecruit() {
    let arr = []
    $(".bookmark-input").map(function (idx, ele) {
        if ($(ele).is(":checked")) {
            let categoryName = $(ele).next("label").text().trim();
            arr.push(categoryName)
        }
    });

    for (const arrElement of arr) {
        console.log(arrElement)
    }
    console.log(recruitDtoList[bookmarkNum])

    let newBookmarkRequestDto = {
        categoryList: arr,
        recruitDto: recruitDtoList[bookmarkNum]
    }
    console.log(newBookmarkRequestDto)

    $.ajax({
        type: "POST",
        url: "/bookmark",
        contentType: "application/json",
        data: JSON.stringify(newBookmarkRequestDto)
    }).done(function () {
        alert("북마크 되었습니다.")
        popClose()
    }).fail(function (data) {
        console.log(data)
        alert("북마크에 실패했습니다.")
    })
}

function deleteBookmark(deleteId, categoryId) {
    $.ajax({
        url: `/my-page/bookmark/${categoryId}?bookmarkId=${deleteId}`,
        type: "DELETE",
    }).done(function (fragment) {
        alert("삭제되었습니다.")
        $("#bookmarkList").replaceWith(fragment);
    }).fail(function(data) {
        alert("삭제에 실패했습니다.")
        console.log(data)
    });
}

function popTagOpen(recruit) {
    if (!isUserExists) {
        alert("로그인이 필요합니다.")
        window.location.href = "/login"
    }
    console.log(recruit);
    bookmarkNum = recruit

    $('#bookmark-modal').modal('show');

    getTagList()
}

function getTagList() {
    $.ajax({
        type: "GET",
        url: "/tag",
        dataType: "text",
    }).done(function (fragment) {
        $("#tagList").replaceWith(fragment);
    });
}

function rollbackTagModalFooter() {
    let addButton = '<button type="button" id="new-tag-button" class="btn btn-primary" onClick="createTagForm()">새 태그</button>' +
                    '<button type="button" id="add-tag-button" class="btn btn-primary" onClick="addTag()">태그 추가</button>'
    $("#tag-footer").html(addButton)
}

function createTagForm() {
    let createTag = '<div id="create-tag">' +
        '<input type="text" id="create-tag-input">' +
        '<button class="btn btn-secondary" onclick="rollbackTagModalFooter()">취소</button>' +
        '<button id="create-tag" class="btn btn-primary" onclick="createTag()">저장</button>' +
        '</div>'
    $("#tag-footer").html(createTag)
}

function createTag() {
    var name = $("#create-tag-input").val();

    var newTagRequestDto = {
        name: name
    }

    $.ajax({
        type: "POST",
        url: `/tag`,
        data: newTagRequestDto
    }).done(function (fragment) {
        $("#tagList").replaceWith(fragment);
    }).fail(function (xhr, status) {
        console.log(xhr)
        alert(xhr.responseJSON["message"])
    });

    rollbackTagModalFooter()
}

function addTag() {
    let arr = []
    $(".tag-input").map(function (idx, ele) {
        if ($(ele).is(":checked")) {
            let tagName = $(ele).next("label").text().trim();
            arr.push(tagName)
        }
    });

    for (const arrElement of arr) {
        console.log(arrElement)
    }

    let taggingRequestDto = {
        "tagList": arr
    }
    console.log(taggingRequestDto)

    $.ajax({
        type: "PUT",
        url: `/tag?bookmarkId=${bookmarkNum}`,
        contentType: "application/json",
        data: JSON.stringify(taggingRequestDto)
    }).done(function (fragment) {
        alert("태그가 추가되었습니다.")
        window.location.reload()
        popClose()
    }).fail(function (data) {
        console.log(data)
        alert("태그 추가에 실패했습니다.")
    })
}

function untag(bookmarkNum, tagId) {

    const unTagRequestDto = {
        tagId: tagId
    }

    $.ajax({
        type: "DELETE",
        url: `/tag?bookmarkId=${bookmarkNum}`,
        contentType: "application/json",
        data: JSON.stringify(unTagRequestDto)
    }).done(function (fragment) {
        alert("태그가 삭제되었습니다.")
        window.location.reload()
    }).fail(function (data) {
        console.log(data)
        alert("태그 삭제에 실패했습니다.")
    })
}