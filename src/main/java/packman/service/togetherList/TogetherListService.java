package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.list.*;
import packman.dto.member.MemberAddDto;
import packman.dto.member.MemberAddResponseDto;
import packman.dto.togetherList.PackerUpdateDto;
import packman.dto.togetherList.TogetherListInviteResponseDto;
import packman.entity.*;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;
import packman.entity.packingList.TogetherAlonePackingList;
import packman.entity.packingList.TogetherPackingList;
import packman.entity.template.Template;
import packman.entity.template.TemplateCategory;
import packman.entity.template.TemplatePack;
import packman.repository.*;
import packman.repository.packingList.AlonePackingListRepository;
import packman.repository.packingList.PackingListRepository;
import packman.repository.packingList.TogetherAlonePackingListRepository;
import packman.repository.packingList.TogetherPackingListRepository;
import packman.repository.template.TemplateCategoryRepository;
import packman.repository.template.TemplateRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static packman.validator.DuplicatedValidator.validateDuplicatedMember;
import static packman.validator.IdValidator.*;
import static packman.validator.LengthValidator.validateListLength;
import static packman.validator.Validator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final PackingListRepository packingListRepository;
    private final AlonePackingListRepository alonePackingListRepository;
    private final TogetherPackingListRepository togetherPackingListRepository;
    private final FolderPackingListRepository folderPackingListRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final PackRepository packRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final TogetherAlonePackingListRepository togetherAlonePackingListRepository;

    public TogetherListResponseDto createTogetherList(ListCreateDto listCreateDto, Long userId) {
        Long folderId = Long.parseLong(listCreateDto.getFolderId());
        String title = listCreateDto.getTitle();
        LocalDate departureDate = LocalDate.parse(listCreateDto.getDepartureDate(), DateTimeFormatter.ISO_DATE);
        String inviteCode;

        // inviteCode ??????
        do {
            inviteCode = RandomStringUtils.randomAlphanumeric(5);
        } while (togetherPackingListRepository.existsByInviteCode(inviteCode));

        // ?????? ??????
        User user = validateUserId(userRepository, userId);

        // ?????? ?????? ?????? X ?????? ??????????????? ?????? or ???????????? ?????? ????????? ??????
        Folder folder = validateUserFolder(folderRepository, folderId, userId, false);

        // ?????? ????????? ??????
        validateListLength(title);

        // ??????????????? ??????
        List<PackingList> packingLists = new ArrayList<>();
        packingLists.add(new PackingList(title, departureDate));
        packingLists.add(new PackingList(title, departureDate));

        List<PackingList> savedLists = packingListRepository.saveAll(packingLists);
        PackingList savedTogetherList = savedLists.get(0); // (??????)???????????????
        PackingList savedMyList = savedLists.get(1); // (??????)???????????????

        // ?????? ??????
        Group savedGroup = groupRepository.save(new Group());

        // ??????-?????? ??????
        userGroupRepository.save(new UserGroup(user, savedGroup));

        // ?????? ??????????????? ??????
        TogetherPackingList savedTogetherPackingList = togetherPackingListRepository.save(new TogetherPackingList(savedTogetherList, savedGroup, inviteCode));

        // ?????? ??????????????? ??????
        AlonePackingList savedMyPackingList = alonePackingListRepository.save(new AlonePackingList(savedMyList, false));

        // ??????-?????? ????????? ??????
        folderPackingListRepository.save(new FolderPackingList(folder, savedMyPackingList));

        // ??????-?????? ??????????????? ??????
        TogetherAlonePackingList savedTogetherAloneList = togetherAlonePackingListRepository.save(new TogetherAlonePackingList(savedTogetherPackingList, savedMyPackingList));

        // ?????? ?????????????????? ?????? ???????????? ??????
        savedMyList.addCategory(new Category(savedMyList, "??????"));

        // ????????? ??????
        if (listCreateDto.getTemplateId().equals("")) { //????????? X
            savedTogetherList.addCategory(new Category(savedTogetherList, "??????"));
        } else { // ????????? O
            // ?????? ???????????? ???????????? ?????? ??????
            Template template = validateTemplateId(templateRepository, Long.parseLong(listCreateDto.getTemplateId()));

            List<TemplateCategory> categories = template.getCategories();
            categories.forEach(m -> {
                Category savedCategory = categoryRepository.save(new Category(savedTogetherList, m.getName()));
                savedTogetherList.addCategory(savedCategory);

                List<TemplatePack> packs = templateCategoryRepository.findById(m.getId()).get().getTemplatePacks();
                packs.forEach(n -> {
                    savedCategory.addPack(new Pack(savedCategory, n.getName()));
                });
            });
        }

        ListResponseMapping savedMyIdCategories = packingListRepository.findByIdAndTitle(savedMyList.getId(), savedMyList.getTitle());
        ListResponseMapping savedTogetherCategories = packingListRepository.findByIdAndTitle(savedTogetherList.getId(), savedTogetherList.getTitle());

        TogetherListDto togetherListDto = TogetherListDto.builder()
                .id(Long.toString(savedTogetherPackingList.getId()))
                .groupId(Long.toString(savedTogetherPackingList.getGroup().getId()))
                .category(savedTogetherCategories.getCategory())
                .inviteCode(savedTogetherPackingList.getInviteCode())
                .isSaved(savedTogetherList.getIsSaved()).build();

        TogetherListResponseDto togetherListResponseDto = TogetherListResponseDto.builder()
                .id(Long.toString(savedTogetherAloneList.getId()))
                .title(savedTogetherList.getTitle())
                .departureDate(savedTogetherList.getDepartureDate().toString())
                .togetherPackingList(togetherListDto)
                .myPackingList(savedMyIdCategories).build();

        return togetherListResponseDto;
    }

    public void deleteTogetherList(Long userId, Long folderId, List<Long> listIds) {
        List<Pack> packs = new ArrayList<>();
        List<UserGroup> userGroups = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<PackingList> lists = new ArrayList<>(); //??????????????? ????????? packingList?????? ?????? ?????????

        // ?????? ?????? ??????, ?????? ??????????????? ???????????? ??????
        validateUserFolder(folderRepository, folderId, userId, false);

        // ?????? ?????? ?????????, ???????????? ??????????????? ??????
        List<TogetherAlonePackingList> togetherAlonePackingLists = validateTogetherListIds(togetherAlonePackingListRepository, listIds);

        // ?????? ????????? ?????? ?????? ??????????????? id ??????
        List<Long> aloneListIds = togetherAlonePackingLists.stream().map(linkList -> linkList.getAlonePackingList().getId()).collect(Collectors.toList());

        // ?????? ???????????? ?????? ?????? ????????? ??????
        List<FolderPackingList> folderPackingLists = validateFolderLists(folderPackingListRepository, folderId, aloneListIds);

        togetherAlonePackingLists.forEach(linkList -> {
            TogetherPackingList togetherList = linkList.getTogetherPackingList();

            // packer == user??? ?????? ??????
            packs.addAll(packRepository.findByCategory_PackingListAndPackerId(togetherList.getPackingList(), userId));

            // ????????? ??????-??????(????????? ????????? ??? ??????-??????) ??????
            userGroups.add(userGroupRepository.findByUserIdAndGroup(userId, togetherList.getGroup()));

            //????????? ?????? ??????????????? ??????
            lists.add(linkList.getAlonePackingList().getPackingList());

            //?????? ??????????????? ????????? ??????-?????? ?????? 1???(????????? ????????? ??????) ??????????????? ?????? == ?????? ????????????????????? ???????????????
            if (userGroupRepository.findByGroup(togetherList.getGroup()).size() == 1) {
                // ????????? ???????????? ????????? ????????? ?????? ??????
                groups.add(togetherList.getGroup());
                // ????????? ?????? ??????????????? ??????
                lists.add(togetherList.getPackingList());
            }
        });

        // ??????-?????? ??????
        userGroupRepository.deleteAllInBatch(userGroups);

        // ??????-??????????????? ??????
        folderPackingListRepository.deleteAllInBatch(folderPackingLists);

        // ??????-?????? ??????????????? ??????
        togetherAlonePackingListRepository.deleteAllInBatch(togetherAlonePackingLists);

        // ??????????????? isDeleted ??????
        packingListRepository.updateListIsDeletedTrue(lists);

        // packer null ??????
        if (!packs.isEmpty()) {
            packRepository.updatePackerNull(packs);
        }

        if (!groups.isEmpty()) {
            // ?????? ?????????????????? groupId null??? set
            togetherPackingListRepository.updateGroupNull(groups);
            // ?????? ??????
            groupRepository.deleteAllInBatch(groups);
        }
    }

    public TogetherListInviteResponseDto getInviteTogetherList(Long userId, String inviteCode) {

        // invitecode??? ???????????? ????????????????????? ??????
        TogetherPackingList togetherPackingList = validateTogetherPackingInviteCode(togetherPackingListRepository, inviteCode);

        // ?????? ????????? ???????????? ??????
        Optional<UserGroup> userGroup = userGroupRepository.findByGroupAndUserId(togetherPackingList.getGroup(), userId);
        if (userGroup.isPresent()) {
            TogetherAlonePackingList togetherAlonePackingList = togetherAlonePackingListRepository.findByTogetherPackingListAndAlonePackingListFolderPackingListFolderUserId(togetherPackingList, userId);
            return new TogetherListInviteResponseDto(String.valueOf(togetherAlonePackingList.getId()), true);
        } else {
            TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListByTogetherList(togetherAlonePackingListRepository, togetherPackingList);
            return new TogetherListInviteResponseDto(String.valueOf(togetherAlonePackingList.getId()), false);
        }
    }

    public DetaildTogetherListResponseDto getTogetherList(Long listId, Long userId) {
        // ???????????? ?????? ????????????????????? ??????
        TogetherAlonePackingList togetherAloneList = validateTogetherAlonePackingListIdInDetail(togetherAlonePackingListRepository, listId);
        // ????????? ?????? ????????????????????? ?????? ??? folderId ??????
        FolderPackingList folderPackingList = validateUserList(folderPackingListRepository, userId, togetherAloneList.getAlonePackingList().getId());

        ListResponseMapping myIdCategories = packingListRepository.findProjectionById(togetherAloneList.getAlonePackingList().getId());
        ListResponseMapping togetherIdCategories = packingListRepository.findProjectionById(togetherAloneList.getTogetherPackingList().getId());

        TogetherListDto togetherListDto = TogetherListDto.builder()
                .id(Long.toString(togetherAloneList.getTogetherPackingList().getId()))
                .groupId(Long.toString(togetherAloneList.getTogetherPackingList().getGroup().getId()))
                .category(togetherIdCategories.getCategory())
                .inviteCode(togetherAloneList.getTogetherPackingList().getInviteCode())
                .isSaved(togetherAloneList.getTogetherPackingList().getPackingList().getIsSaved()).build();

        DetaildTogetherListResponseDto detaildTogetherListResponseDto= DetaildTogetherListResponseDto.builder()
                .id(togetherAloneList.getId().toString())
                .folderId(folderPackingList.getFolder().getId().toString())
                .togetherPackingList(togetherListDto)
                .myPackingList(myIdCategories).build();

        return detaildTogetherListResponseDto;
    }

    public ListResponseMapping updatePacker(PackerUpdateDto packerUpdateDto, Long userId) {
        Long packerId = Long.parseLong(packerUpdateDto.getPackerId());
        // ?????? ??????
        User user = validateUserId(userRepository, userId);

        // ????????? ?????? ????????????????????? ??????
        TogetherPackingList togetherPackingList = validateUserTogetherPackingListId(togetherPackingListRepository, Long.parseLong(packerUpdateDto.getListId()), user);

        // ???????????? ???????????? ????????? ??????
        Pack pack = validateListPack(packRepository, togetherPackingList.getPackingList(), Long.parseLong(packerUpdateDto.getPackId()));

        if(!packerId.equals(userId)){
            // packer??? ?????? ?????? user?????? userGroup??? ??????????????? ??????
            UserGroup userGroup = validateUserInUserGroup(userGroupRepository, togetherPackingList.getGroup(), packerId);
            pack.setPacker(userGroup.getUser());
        }else{
            pack.setPacker(user);
        }

        return packingListRepository.findProjectionById(togetherPackingList.getId());
    }

    public MemberAddResponseDto addMember(MemberAddDto memberAddDto, Long userId) {
        User user = validateUserId(userRepository, userId);
        TogetherAlonePackingList togetherAlonePackingList = validateTogetherAlonePackingListId(togetherAlonePackingListRepository, Long.parseLong(memberAddDto.getListId()));

        TogetherPackingList togetherPackingList = togetherAlonePackingList.getTogetherPackingList();
        validateTogetherListDeleted(togetherPackingList);

        // ?????? ????????? ????????? ?????? ??????????????? ??????
        Group group = togetherPackingList.getGroup();
        validateDuplicatedMember(group, user);

        // user_group ??????
        UserGroup userGroup = new UserGroup(user, group);
        userGroupRepository.save(userGroup);

        // ?????? ??????
        Folder defaultFolder = folderRepository.findByUserIdAndNameAndIsAloned(userId, "??????", false)
                .orElseGet(() -> {
                    Folder folder = new Folder(user, "??????", false);
                    folderRepository.save(folder);
                    return folder;
                });

        PackingList packingList = togetherPackingList.getPackingList();

        // ?????? ??? ?????? ?????? ??????
        PackingList newPackingList = new PackingList(packingList.getTitle(), packingList.getDepartureDate());
        packingListRepository.save(newPackingList);

        AlonePackingList myPackingList = new AlonePackingList(newPackingList, false);
        alonePackingListRepository.save(myPackingList);

        newPackingList.setAlonePackingList(myPackingList);

        // ?????? ????????????
        Category category = new Category(newPackingList, "??????");
        categoryRepository.save(category);

        TogetherAlonePackingList newTogetherAlonePackingList = new TogetherAlonePackingList(togetherPackingList, myPackingList);
        togetherAlonePackingListRepository.save(newTogetherAlonePackingList);

        FolderPackingList folderPackingList = new FolderPackingList(defaultFolder, myPackingList);
        folderPackingListRepository.save(folderPackingList);

        return new MemberAddResponseDto(newTogetherAlonePackingList.getId().toString());
    }
}
