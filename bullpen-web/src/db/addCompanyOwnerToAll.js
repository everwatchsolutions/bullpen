
db.opening.update({"ownerCompany":{"$exists":false}},{"$set":{"ownerCompany" : DBRef("company", ObjectId("5408ce568884ca369bbbb84a"))}},false, true)
db.candidate.update({"ownerCompany":{"$exists":false}},{"$set":{"ownerCompany" : DBRef("company", ObjectId("5408ce568884ca369bbbb84a"))}},false, true)