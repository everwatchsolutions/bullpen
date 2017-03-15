//In order to delete a company, first you have to find out it's id
db.company.find({"name":"Sunkencity Software"}, {"_id":1});

//Replace the ID here, then copy and paste everything below this comment into the mongo shell. 
var id = "54986ff1de8ef1aee0fcc335";
var query = {"ownerCompany": DBRef("company", ObjectId(id))};
db.candidate.remove(query);
db.opening.remove(query);
db.companyInvite.remove({"invitedToCompany": DBRef("company", ObjectId(id))});
db.skill.remove(query);
db.certification.remove(query);
db.category.remove(query);
db.workflow.remove(query);
db.user.remove({"company": DBRef("company", ObjectId(id))});
db.company.remove({"_id": ObjectId(id)});

