var bookmarkNum;
var isUserExists;

function popOpen(recruit) {
    if (!isUserExists) {
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
    }
    console.log(recruit);
    bookmarkNum = recruit;

    $('#bookmark-modal').modal('show');

    getCategoryList();
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

    const name = $("#add-input").val();

    const newCategoryRequestDto = {
        name: name
    };

    $.ajax({
        type: "POST",
        url: "/category",
        data: newCategoryRequestDto
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    }).fail(function (xhr, status) {
        console.log(xhr);
        alert(xhr.responseJSON["message"]);
    });

    rollbackModalFooter();
}

function rollbackModalFooter() {
    const addButton = '<button id="add-category-button" class="btn btn-primary" onclick=addCategory()>카테고리 추가</button>' +
        '<button id="bookmark-button" class="btn btn-primary" onclick=bookmarkRecruit()>북마크 추가</button>';
    $("#bookmark-footer").html(addButton);
}

function addCategory() {
    const addCategory = '<div id="add-category">' +
        '<input type="text" id="add-input">' +
        '<button class="btn btn-secondary" onclick="rollbackModalFooter()">취소</button>' +
        '<button id="add-category" class="btn btn-primary" onclick="createCategory()">저장</button>' +
        '</div>';
    $("#bookmark-footer").html(addCategory);
}

function deleteCategory(deleteId) {
    $.ajax({
        url: `/my-page/bookmark/?categoryId=${deleteId}`,
        type: "DELETE",
    }).done(function (fragment) {
        alert("삭제되었습니다.");
        $("#categoryList").replaceWith(fragment);
    }).fail(function(data) {
        alert("삭제에 실패했습니다.");
        console.log(data);
    });
}

function bookmarkRecruit() {
    const arr = [];
    $(".bookmark-input").map(function (idx, ele) {
        if ($(ele).is(":checked")) {
            const categoryName = $(ele).next("label").text().trim();
            arr.push(categoryName);
        }
    });

    for (const arrElement of arr) {
        console.log(arrElement);
    }
    console.log(recruitDtoList[bookmarkNum]);

    const newBookmarkRequestDto = {
        categoryList: arr,
        recruitDto: recruitDtoList[bookmarkNum]
    };
    console.log(newBookmarkRequestDto);

    $.ajax({
        type: "POST",
        url: "/bookmark",
        contentType: "application/json",
        data: JSON.stringify(newBookmarkRequestDto)
    }).done(function () {
        alert("북마크 되었습니다.");
        popClose();
    }).fail(function (data) {
        console.log(data);
        alert("북마크에 실패했습니다.");
    })
}

function deleteBookmark(deleteId, categoryId) {
    $.ajax({
        url: `/my-page/bookmark/${categoryId}?bookmarkId=${deleteId}`,
        type: "DELETE",
    }).done(function (fragment) {
        alert("삭제되었습니다.");
        $("#bookmarkList").replaceWith(fragment);
    }).fail(function(data) {
        alert("삭제에 실패했습니다.");
        console.log(data);
    });
}

function getTagList() {
    $.ajax({
        type: "GET",
        url: "/bookmark/tag",
        dataType: "text",
    }).done(function (fragment) {
        $("#tagList").replaceWith(fragment);
    });
}

function rollbackTagBtn(bookmarkId) {
    const addButton = `<button class="btn btn-secondary btn-sm" id="tag-btn${bookmarkId}"` +
                              ` onclick="addTagForm(${bookmarkId})">+</button>`

    $("#add-tag").replaceWith(addButton);
}

function addTagForm(bookmarkId) {
    if (!isUserExists) {
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
    }
    const createTag = `<div id="add-tag${bookmarkId}">` +
        `<input type="text" id="add-tag-input${bookmarkId}">` +
        `<button class="btn btn-secondary btn-sm" onclick="rollbackTagBtn(${bookmarkId})">취소</button>` +
        `<button id="add-tag${bookmarkId}" class="btn btn-primary btn-sm" onclick="addTag(${bookmarkId})">저장</button>` +
        '</div>';
    $(`#tag-btn${bookmarkId}`).replaceWith(createTag);
}

function addTag(bookmarkId) {
    const name = $(`#add-tag-input${bookmarkId}`).val();

    const taggingRequestDto = {
        tagName: name
    };

    console.log(taggingRequestDto)

    $.ajax({
        type: "POST",
        url: `/tag?bookmarkId=${bookmarkId}`,
        contentType: "application/json",
        data: JSON.stringify(taggingRequestDto)
    }).done(function (fragment) {
        window.location.reload();
    }).fail(function (xhr, status) {
        console.log(xhr);
        alert(xhr.responseJSON["message"]);
    });

    rollbackTagBtn(bookmarkId);
}

function untag(bookmarkNum, tagId) {

    const unTagRequestDto = {
        tagId: tagId
    };

    $.ajax({
        type: "DELETE",
        url: `/bookmark/tag?bookmarkId=${bookmarkNum}`,
        contentType: "application/json",
        data: JSON.stringify(unTagRequestDto)
    }).done(function (fragment) {
        alert("태그가 삭제되었습니다.");
        window.location.reload();
    }).fail(function (data) {
        console.log(data);
        alert("태그 삭제에 실패했습니다.");
        window.location.reload();
    })
}