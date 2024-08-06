package com.example.data.repositories.activity

import com.example.data.Mapper

//class GroupCacheMapper: Mapper<Pair<TaskGroupEntity, Int>, Group> {
//    override fun mapFromEntity(type: Pair<TaskGroupEntity, Int>): Group {
//        with(type.first) {
//            return Group(
//                groupId = groupId,
//                groupName = groupName,
//                groupColor = groupColor,
//                tasksCount = type.second,
//                groupImage = groupImage
//            )
//        }
//    }
//
//    override fun mapToEntity(type: Group): Pair<TaskGroupEntity, Int> {
//        with(type) {
//            return Pair(
//                first = TaskGroupEntity(
//                    groupId = groupId,
//                    groupName = groupName,
//                    groupColor = groupColor,
//                    groupImage = groupImage
//                ),
//                second = tasksCount
//            )
//        }
//    }
//
//    fun mapToDatabaseEntity(group: Group): TaskGroupEntity {
//        with(group) {
//            return TaskGroupEntity(
//                groupId = groupId,
//                groupName = groupName,
//                groupColor = groupColor,
//                groupImage = groupImage
//            )
//        }
//    }
//
//}