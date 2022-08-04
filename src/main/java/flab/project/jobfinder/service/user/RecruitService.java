package flab.project.jobfinder.service.user;

import flab.project.jobfinder.dto.bookmark.*;
import flab.project.jobfinder.dto.page.PageDto;
import flab.project.jobfinder.dto.recruit.RecruitDto;
import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.entity.recruit.RecruitTag;
import flab.project.jobfinder.entity.user.User;
import flab.project.jobfinder.repository.RecruitRepository;
import flab.project.jobfinder.service.user.pagination.BookmarkPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final BookmarkPagination bookmarkPagination;


    @Transactional
    public BookmarkResponseDto bookmark(User user, RecruitDto recruitDto, Category category) {
        Recruit bookmark = recruitRepository.save(recruitDto.toEntity(category, user));
        return new BookmarkResponseDto(bookmark.getId(), new RecruitDto(bookmark), null);
    }

    @Transactional
    public long unbookmark(Recruit bookmark) {
        recruitRepository.delete(bookmark);
        return bookmark.getId();
    }

    public BookmarkPageDto findByCategory(User user, Long categoryId, Pageable pageable) {
        Page<Recruit> page = recruitRepository.findRecruits(user, categoryId, pageable);
        List<BookmarkResponseDto> bookmarkList = page.get()
                .map(recruit -> new BookmarkResponseDto(recruit.getId(), new RecruitDto(recruit), getTagsDtoByBookmark(recruit)))
                .toList();
        PageDto pageDto = bookmarkPagination.toPageDto(page);
        return BookmarkPageDto.builder()
                .bookmarkList(bookmarkList)
                .pageDto(pageDto)
                .build();
    }

    public Optional<Recruit> findById(User user, Long bookmarkId) {
        return Optional.ofNullable(recruitRepository.findRecruit(user, bookmarkId));
    }

    public Optional<Recruit> findByCategoryIdAndBookmarkId(User user, Long categoryId, Long bookmarkId) {
        return Optional.ofNullable(recruitRepository.findRecruit(user, categoryId, bookmarkId));
    }

    private List<TagResponseDto> getTagsDtoByBookmark(Recruit bookmark) {
        return bookmark.getRecruitTagList()
                .stream()
                .map(RecruitTag::getTag)
                .map(TagResponseDto::new)
                .toList();
    }
}
