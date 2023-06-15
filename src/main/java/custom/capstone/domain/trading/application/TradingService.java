package custom.capstone.domain.trading.application;

import custom.capstone.domain.members.dao.MemberRepository;
import custom.capstone.domain.members.domain.Member;
import custom.capstone.domain.posts.dao.PostRepository;
import custom.capstone.domain.posts.domain.Post;
import custom.capstone.domain.trading.dao.TradingRepository;
import custom.capstone.domain.trading.domain.Trading;
import custom.capstone.domain.trading.dto.TradingSaveRequestDto;
import custom.capstone.domain.trading.dto.TradingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class TradingService {
    private final TradingRepository tradingRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveTrading(final TradingSaveRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.postId())
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));

        Member buyer = memberRepository.findById(requestDto.buyerId())
                .orElseThrow(() -> new NotFoundException("구매자를 찾을 수 없습니다."));

        Member seller = memberRepository.findById(requestDto.sellerId())
                .orElseThrow(() -> new NotFoundException("판매자를 찾을 수 없습니다."));


        Trading trading = Trading.builder()
                .post(post)
                .buyer(buyer)
                .seller(seller)
                .build();

        return tradingRepository.save(trading).getId();
    }

    @Transactional
    public Long updateTrading(final Long tradingId, final TradingUpdateRequestDto requestDto) {
        Trading trading = tradingRepository.findById(tradingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래를 찾을 수 없습니다."));

        trading.update(requestDto.post(), requestDto.buyer(), requestDto.buyer(), requestDto.status());

        return tradingId;
    }


    public Trading findById(final Long tradingId) {
        return tradingRepository.findById(tradingId)
                .orElseThrow(NullPointerException::new);
    }

    @Transactional
    public void deleteTrading(final Long tradingId) {
        Trading trading = tradingRepository.findById(tradingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래를 찾을 수 없습니다."));

        tradingRepository.delete(trading);
    }
}
